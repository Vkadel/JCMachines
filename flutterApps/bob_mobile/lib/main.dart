import 'package:bob_mobile/auth.dart';
import 'package:bob_mobile/constants.dart';
import 'package:bob_mobile/data_type/user.dart';
import 'package:bob_mobile/firestore.dart';
import 'package:bob_mobile/provider.dart';
import 'package:bob_mobile/qanda.dart';
import 'package:bob_mobile/validators.dart';
import 'package:bob_mobile/widgets/google_signin_button.dart';
import 'package:bob_mobile/widgets/loading_indicator_full_screen.dart';
import 'package:bob_mobile/widgets/rounded_edge_button.dart';
import 'package:bob_mobile/widgets/rounded_edge_button_survey.dart';
import 'package:bob_mobile/widgets/scrollable_widget_window.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'dashboard_page.dart';
import 'login_page.dart';
import 'personality_test_page.dart';
import 'data_type/question.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return Quanda(
      progress: 0,
      a_pressed: false,
      b_pressed: false,
      permanent: <Question>[],
      child: Provider(
        auth: Auth(),
        fireBase: MBobFireBase(),
        child: MaterialApp(
          title: 'Flutter Demo',
          theme: ThemeData(
            secondaryHeaderColor: Colors.blueAccent,
            accentColor: Colors.blueAccent,
            primaryColor: Colors.deepOrange,
            scaffoldBackgroundColor: Colors.deepOrange[50],
          ),
          home: EntryPage(title: 'Battle of the Books'),
          routes: <String, WidgetBuilder>{
            '/home': (BuildContext context) => HomePage(title: 'Home Page'),
            '/personality_test': (BuildContext context) =>
                PersonalitySurveyPage(title: 'Tell your tale')
          },
        ),
      ),
    );
  }
}

class EntryPage extends StatefulWidget {
  EntryPage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _EntryPageState createState() => _EntryPageState();
}

class _EntryPageState extends State<EntryPage> {
  _EntryPageState();
  String _user;

  @override
  Widget build(BuildContext context) {
    final Auth auth = Provider.of(context).auth;

    return StreamBuilder<FirebaseUser>(
      stream: auth.onAuthStateChanged,
      builder: (context, AsyncSnapshot<FirebaseUser> snapshot) {
        print(snapshot.connectionState);
        if (snapshot.connectionState == ConnectionState.active) {
          print("connection state: Connection is active");
          String userid;
          String userEmail;
          if (snapshot.data != null) {
            userid = snapshot.data.uid;
            userEmail = snapshot.data.email;
            print('SNAPSHOT Connection id: $userid');
            final bool loggedIn = snapshot.hasData;
            return (loggedIn && snapshot.data != null)
                ? HomePage(
                    email: userEmail,
                    uid: userid,
                    title: 'my Home Page',
                  )
                : LoginPage();
          } else {
            return new Container(
              width: 0.0,
              height: 0.0,
            );
          }
        } else {
          return mLoadingIndicatorFullScreen(context, snapshot.connectionState);
        } //Condition to check when data comes back from the Auth//Condition to load when waiting for data to return
      },
    );
  }
}

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title, this.uid, this.email}) : super(key: key);
  //update the constructor to include the uid
  final String title;
  final String uid; //include this
  final String email;

  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    final Auth auth = Provider.of(context).auth;
    final MBobFireBase mBobFireBase = Provider.of(context).fireBase;

    if (widget.uid != null) {
      print('This is the widget INFO: $widget.uid ');

      return StreamBuilder<QuerySnapshot>(
        stream: mBobFireBase.get_userprofile(widget.uid),
        builder:
            // ignore: missing_return
            (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
          print('SNAPSHOT Connection STARTED');

          return snapshot.hasError
              ? Container(
                  child: Text('Snapshot from user profile has errors'),
                )
              : (snapshot.connectionState == ConnectionState.active &&
                      snapshot.data != null &&
                      snapshot.hasData)
                  ? userPlayReadyVerification(snapshot)
                  : Container(child: CircularProgressIndicator());
        },
      );
    } else {
      return Scaffold(
        appBar: AppBar(
          title: Text('The Home Page'),
          actions: <Widget>[
            FlatButton(
                onPressed: () async {
                  try {
                    Auth auth = Provider.of(context).auth;
                    await auth.signOut();
                  } catch (e) {
                    print(e);
                  }
                },
                child: Text('Sign outs'))
          ],
        ),
        body: Text('Welcome'),
      );
    }
  }

  bool UserIsIntro() {
    bool intro;
    if (Quanda.of(context).myUser.personality['value_i'] != 0 ||
        Quanda.of(context).myUser.personality['value_e'] != 0) {
      if (Quanda.of(context).myUser.personality['value_i'] >
          Quanda.of(context).myUser.personality['value_e']) {
        intro = true;
      } else {
        intro = false;
      }
    } else {
      intro = false;
    }
    return intro;
  }

  bool UserIsExtro() {
    bool isExtro;
    if (Quanda.of(context).myUser.personality['value_i'] != 0 ||
        Quanda.of(context).myUser.personality['value_e'] != 0) {
      if (Quanda.of(context).myUser.personality['value_i'] <
          Quanda.of(context).myUser.personality['value_e']) {
        isExtro = true;
      } else {
        isExtro = false;
      }
    } else {
      isExtro = false;
    }
    return isExtro;
  }

  bool user_has_personality() {
    if (UserIsExtro() || UserIsIntro()) {
      return true;
    } else {
      return false;
    }
  }

  Widget createUserProfile() {
    print('SNAPSHOT Connection Returned Empty');
    Provider.of(context)
        .fireBase
        .createUserProfile(widget.uid, widget.email, context);
    return Container(
      child: Text('created user Profile'),
    );
  }

  bool userHasRole() {
    if (Quanda.of(context).myUser.role == 0) {
      return false;
    } else {
      return true;
    }
  }

  Widget userProfileWorkflow(AsyncSnapshot<QuerySnapshot> userProfileSnapShot) {
    //User has user Profile
    int lenght = userProfileSnapShot.data.documents.length;
    print('SNAPSHOT Connection Returned $lenght');
    //Check if User has personality Test
    Quanda.of(context).myUser =
        User.fromJson(userProfileSnapShot.data.documents.first.data);

    //Check if User has personality Test
    if (user_has_personality()) {
      print("User Has personality");
    } else {
      return PersonalitySurveyPage(title: 'Personality Survey');
    }

    if (userHasRole()) {
      print('User Has role......');
    } else {
      return SelectRolePage();
    }
    if (userHasRole() && user_has_personality()) return DashBoardPage();
  }

  Widget userPlayReadyVerification(AsyncSnapshot<QuerySnapshot> snapshot) {
    switch (snapshot.connectionState) {
      case ConnectionState.waiting:
        print('SNAPSHOT Connection waiting');
        return mLoadingIndicatorFullScreen(context, snapshot.connectionState);
        break;
      case ConnectionState.none:
        print('SNAPSHOT Connection NONE');
        return mLoadingIndicatorFullScreen(context, snapshot.connectionState);
        break;
      case ConnectionState.active:
        print('SNAPSHOT Connection Active');
        return snapshot.data.documents.isEmpty
            ? createUserProfile()
            : userProfileWorkflow(snapshot);
        break;
      case ConnectionState.done:
        print('SNAPSHOT Connection done');
        return mLoadingIndicatorFullScreen(context, snapshot.connectionState);
        // ignore: missing_return
        break;
      default:
        return new Container(width: 0.0, height: 0.0);
    }
  }

  Widget buildUserHasUID(MBobFireBase mBobFireBase) {
    return Scaffold(
        appBar: AppBar(
          title: Text('The Home Page'),
          actions: <Widget>[
            FlatButton(
                onPressed: () async {
                  try {
                    Auth auth = Provider.of(context).auth;
                    await auth.signOut();
                  } catch (e) {
                    print(e);
                  }
                },
                child: Text('Sign out'))
          ],
        ),
        body: StreamBuilder<QuerySnapshot>(
          stream: mBobFireBase.get_userprofile(widget.uid),
          builder:
              // ignore: missing_return
              (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
            print('SNAPSHOT Connection STARTED');
            return snapshot.hasError
                ? new Container(
                    child: Text('Error getting data from database'),
                  )
                : userPlayReadyVerification(snapshot);
          },
        ));
  }

  Widget buildUserHasNOId() {
    return Scaffold(
      appBar: AppBar(
        title: Text('The Home Page'),
        actions: <Widget>[
          FlatButton(
              onPressed: () async {
                try {
                  Auth auth = Provider.of(context).auth;
                  await auth.signOut();
                } catch (e) {
                  print(e);
                }
              },
              child: Text('Sign outs'))
        ],
      ),
      body: Text('Welcome'),
    );
  }
}

class SelectRolePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _SelectRoleState();
  }
}

class _SelectRoleState extends State<SelectRolePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Constants().select_your_role_page_title),
      ),
      body: Container(
        child: Text("User needs to pick a hero"),
      ),
    );
  }
}
