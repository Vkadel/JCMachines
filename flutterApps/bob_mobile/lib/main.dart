import 'package:bob_mobile/auth.dart';
import 'package:bob_mobile/firestore.dart';
import 'package:bob_mobile/provider.dart';
import 'package:bob_mobile/validators.dart';
import 'package:bob_mobile/widgets/google_signin_button.dart';
import 'package:bob_mobile/widgets/rounded_edge_button.dart';
import 'package:bob_mobile/widgets/scrollable_widget_window.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return Provider(
      auth: Auth(),
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

    return StreamBuilder<String>(
      stream: auth.onAuthStateChanged,
      builder: (context, AsyncSnapshot<String> snapshot) {
        print("connection state: ");
        print(snapshot.connectionState);
        if (snapshot.connectionState == ConnectionState.active) {
          print("connection state: Connection is active");
          String userid = snapshot.data.toString();
          print('SNAPSHOT Connection id: $userid');
          final bool loggedIn = snapshot.hasData;
          //TODO: Check whether user has profile
          //TODO: Check if user has been evaluated
          return loggedIn
              ? HomePage(
                  uid: userid,
                  title: 'my Home Page',
                )
              : LoginPage();
        } //Condition to check when data comes back from the Auth
        return Center(
          child: CircularProgressIndicator(
            strokeWidth: 5,
          ),
        ); //Condition to load when waiting for data to return
      },
    );
  }
}

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title, this.uid}) : super(key: key);
  //update the constructor to include the uid
  final String title;
  final String uid; //include this

  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    final Auth auth = Provider.of(context).auth;
    final MBobFireBase mBobFireBase = MBobFireBase();

    if (widget.uid != null) {
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
          body: StreamBuilder<QuerySnapshot>(
            stream: mBobFireBase.get_userprofile(widget.uid),
            builder:
                // ignore: missing_return
                (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
              print('SNAPSHOT Connection STARTED');
              if (snapshot.hasError) {
                print("SNAPSHOT has Error");
              } else {
                switch (snapshot.connectionState) {
                  case ConnectionState.waiting:
                    print('SNAPSHOT Connection waiting');
                    break;
                  case ConnectionState.none:
                    print('SNAPSHOT Connection NONE');
                    break;
                  case ConnectionState.active:
                    print('SNAPSHOT Connection Active');
                    return ListView(
                      children: snapshot.data.documents
                          .map((DocumentSnapshot document) {
                        return new ListTile(
                          title: new Text(document['uid']),
                          subtitle: new Text(document['email']),
                        );
                      }).toList(),
                    );
                    break;
                  case ConnectionState.done:
                    print('SNAPSHOT Connection done');
                    // ignore: missing_return
                    break;
                  default:
                }
              }
            },
          ));
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
}

class LoginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return _LoginPageState();
  }
}

class _LoginPageState extends State<LoginPage> {
  final formKey = GlobalKey<FormState>();
  String _email, _password;
  FormType _formtype = FormType.login;
  Text _tittle_register = new Text('Register to Battle');
  Text _tittle_login = new Text('Log in Champion');

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        //TODO: Consolidate Strings
        title: _tittle_login,
      ),
      body: Form(
        key: formKey,
        child: Center(
            child: myScrollableWindow(
          context,
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            mainAxisSize: MainAxisSize.max,
            children: buildLogoTitle() + buildInputs() + buildButtons(),
          ),
        )),
      ),
    );
  }

  bool validate() {
    final form = formKey.currentState;
    if (form.validate()) {
      form.save();
      return true;
    } else {
      return false;
    }
  }

  //After user entered information is validated the user can submit

  void submit() async {
    if (validate()) {
      final auth = Provider.of(context).auth;
      try {
        if (_formtype == FormType.login) {
          String userid =
              await auth.signInWithEmailAndPassword(_email.trim(), _password);
          print('signed in $userid');
        } else {
          String userid = await auth.createUserWithEmailAndPassword(
              _email.trim(), _password);
          print('Registered in $userid');
        }
      } catch (e) {
        print(e);
      }
    }
  }

  void switchFormState(FormType state) {
    print('Switching state to: $state');
    formKey.currentState.reset();
    if (state == FormType.login) {
      setState(() {
        print('Switching state to: register');
        _formtype = FormType.register;
      });
    } else {
      setState(() {
        _formtype = FormType.login;
      });
    }
  }

  List<Widget> buildInputs() {
    return [
      Padding(
        padding: const EdgeInsets.fromLTRB(16, 0, 16, 0),
        child: TextFormField(
          validator: EmailValidator.validate,
          decoration: InputDecoration(labelText: 'email'),
          onSaved: (value) => _email = value,
        ),
      ),
      Padding(
        padding: const EdgeInsets.fromLTRB(16, 0, 16, 0),
        child: TextFormField(
          validator: PasswordValidator.validate,
          decoration: InputDecoration(labelText: 'password'),
          obscureText: true,
          onSaved: (value) => _password = value,
        ),
      ),
    ];
  }

  List<Widget> buildButtons() {
    //Login
    if (_formtype == FormType.login) {
      return [
        //TODO: String
        FormattedRoundedButton('Login with email', submit),
        //TODO: String
        GoogleSingInButton('Log in with Google', Provider.of(context).auth),
        FlatButton(
          onPressed: () {
            switchFormState(_formtype);
          },
          //TODO: String
          child: Text('Go to Register Account'),
        ),
        FlatButton(
          onPressed: () {
            Provider.of(context).auth.passwordReset(_email);
          },
          //TODO: String
          child: Text('Forgot my Password'),
        ),
      ];
    } else {
      //Register
      return [
        //TODO: String
        FormattedRoundedButton('Create Account with email', submit),
        //TODO: String
        GoogleSingInButton(
            'Register with your Google Account', Provider.of(context).auth),
        FlatButton(
          //TODO: String
          child: Text('Go to Login'),
          onPressed: () {
            switchFormState(_formtype);
          },
        ),
      ];
    }
  }

  List<Widget> buildLogoTitle() {
    return [
      Column(
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(10),
            child: Text(
              'Battle of the Books',
              style: TextStyle(
                  fontSize: 30,
                  color: Colors.deepOrange,
                  fontWeight: FontWeight.w200),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(1),
            child: Image(
              image: AssetImage('assets/bob_logo.png'),
              width: 200,
              height: 200,
            ),
          )
        ],
      ),
    ];
  }
}

class PersonalitySurveyPage extends StatefulWidget {
  PersonalitySurveyPage({Key key, this.title, this.uid}) : super(key: key);
  //update the constructor to include the uid
  final String title;
  final String uid;

  @override
  State<StatefulWidget> createState() => _PersonalitySurveyState();
}

class _PersonalitySurveyState extends State<PersonalitySurveyPage> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return null;
  }
}
