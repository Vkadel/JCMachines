import 'package:bob_mobile/auth.dart';
import 'package:bob_mobile/constants.dart';
import 'package:bob_mobile/data_type/personality_questions.dart';
import 'package:bob_mobile/data_type/user.dart';
import 'package:bob_mobile/firestore.dart';
import 'package:bob_mobile/provider.dart';
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

import 'data_type/question.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return Provider(
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
          }
          final bool loggedIn = snapshot.hasData;
          return loggedIn
              ? HomePage(
                  email: userEmail,
                  uid: userid,
                  title: 'my Home Page',
                )
              : LoginPage();
        } //Condition to check when data comes back from the Auth
        return mLoadingIndicatorFullScreen(
            context,
            snapshot
                .connectionState); //Condition to load when waiting for data to return
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
              if (snapshot.hasError) {
                print("SNAPSHOT has Error");
              } else {
                switch (snapshot.connectionState) {
                  case ConnectionState.waiting:
                    print('SNAPSHOT Connection waiting');
                    return mLoadingIndicatorFullScreen(
                        context, snapshot.connectionState);
                    break;
                  case ConnectionState.none:
                    print('SNAPSHOT Connection NONE');
                    return mLoadingIndicatorFullScreen(
                        context, snapshot.connectionState);
                    break;
                  case ConnectionState.active:
                    print('SNAPSHOT Connection Active');
                    if (snapshot.data.documents.isEmpty) {
                      //Don't have user profile
                      print('SNAPSHOT Connection Returned Empty');
                      Provider.of(context)
                          .fireBase
                          .createUserProfile(widget.uid, widget.email);
                      return Container(
                        child: Text('created user Profile'),
                      );
                    } else {
                      //User has user Profile
                      int lenght = snapshot.data.documents.length;
                      print('SNAPSHOT Connection Returned $lenght');
                      //Check if User has personality Test
                      var user =
                          User.fromJson(snapshot.data.documents.first.data);
                      //Check if User has personality Test
                      if (user.personality == 0) {
                        return new Container(
                          child: PersonalitySurveyPage(
                              title: 'Personality Survey'),
                        );
                      } else if (user.personality != 0) {
                        return new Container(
                          child: Text(
                              'User is ready to move to build HOME SCREEN'),
                        );
                      }
                    }
                    break;
                  case ConnectionState.done:
                    print('SNAPSHOT Connection done');
                    return mLoadingIndicatorFullScreen(
                        context, snapshot.connectionState);
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
    return _LoginPageState();
  }
}

class _LoginPageState extends State<LoginPage> {
  final formKey = GlobalKey<FormState>();
  FormType _formtype = FormType.login;
  String _email, _password;
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
  PersonalitySurveyPage({ObjectKey key, this.title, this.user})
      : super(key: key);
  //update the constructor to include the uid
  final String title;
  final User user;
  final formKey = GlobalKey<_PersonalitySurveyState>();

  //TODO: May want to tie progress to user just in case they disco

  @override
  State<StatefulWidget> createState() => _PersonalitySurveyState();
}

class _PersonalitySurveyState extends State<PersonalitySurveyPage> {
  int _progress;
  List<Question> _list_of_questions;
  List<Question> _permanent = <Question>[];
  bool a_pressed;
  bool b_pressed;
  int _result_e;
  int _result_i;
  List<int> _result_e_pointer;
  List<int> _result_i_pointer;
  String _my_text;

  @override
  void initState() {
    _result_e = 0;
    _result_i = 0;
    _progress = 0;
    a_pressed = false;
    b_pressed = false;
    _result_e_pointer = <int>[];
    _result_i_pointer = <int>[];
    _my_text = '_my_text';
  }

  @override
  Widget build(BuildContext context) {
    //get questions
    return StreamBuilder(
      stream: Provider.of(context).fireBase.getQuestions(),
      builder: (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
        if (snapshot.connectionState == ConnectionState.active) {
          try {
            if (snapshot.data != null) {
              _list_of_questions = snapshot.data.documents
                  .toList()
                  .map((DocumentSnapshot doc) => Question.fromJson(doc.data))
                  .toList();
              var question = _list_of_questions.elementAt(0).option_a;
              print('WHY O WHY: $question');
              return BuildSurveyQuestion();
            }
          } catch (e) {
            print(e);
            return Text('Sorry We are missing some nuts and bolts here');
          }
        } else {
          return Container(
            //Connection is not Active yet
            child: CircularProgressIndicator(),
          );
        }
        ;
      },
    );
  }

  Widget BuildSurveyQuestion() {
    List<bool> isSelected;
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: <Widget>[
          Container(
            alignment: Alignment.topCenter,
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 10),
            child: Text(
              _list_of_questions.elementAt(_progress).question,
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 30, fontWeight: FontWeight.w200),
            ),
          ),
          FormattedRoundedButtonSurvey(
              _list_of_questions.elementAt(_progress).option_a,
              press_a,
              a_pressed),
          FormattedRoundedButtonSurvey(
              _list_of_questions.elementAt(_progress).option_b,
              press_b,
              b_pressed),
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Container(
                  alignment: Alignment.bottomRight,
                  child: FlatButton(
                    child: Text('Previous'),
                    onPressed: () {
                      Go_prev();
                    },
                  ),
                ),
                Text('$_my_text'),
                FlatButton(
                  child: Text('Next'),
                  onPressed: () {
                    Go_next();
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void Go_next() {
    clearButs();
    int showing = _progress;
    print('Trying to change state from: $showing');
    setState(() {
      if (_progress == Constants().number_of_questions_personality_test) {
        _progress = 0;
        //TODO: Make sure only one check if done and only happens when
        //When permanent is size 20. Then it pops back into the dashboard.
        return;
      } else {
        if (_progress == Constants().number_of_questions_personality_test - 1) {
          //Calculate Personality
          calculate_intro_extro_perso();
        }
        if (_progress < Constants().number_of_questions_personality_test - 1) {
          print('Trying to change state from: $showing');
          _progress = _progress + 1;
        }
      }
      if (_progress < _permanent.length) {
        update_buttons_with_current_selections();
      }
    });
  }

  void Go_prev() {
    clearButs();
    setState(() {
      if (_progress == 0) {
        _progress = Constants().number_of_questions_personality_test - 1;
      } else {
        if (_progress > 0) {
          print('Trying to change state');
          _progress = _progress - 1;
          //Clean last Item
        }
      }
      update_buttons_with_current_selections();
    });
  }

  void press_a() {
    print('setting state to A');
    b_pressed = false;
    a_pressed = true;
    Question question = _list_of_questions.elementAt(_progress);
    setState(() {
      if (_progress == 0 || _permanent.length <= _progress) {
        _permanent.insert(_progress, question);
      } else {
        _permanent[_progress] = question;
        update_buttons_with_current_selections();
      }
      _permanent[_progress].selection_b = 0;
      _permanent[_progress].selection_a = 1;
      printit();
    });
  }

  void press_b() {
    print('setting state to B');
    b_pressed = true;
    a_pressed = false;
    Question question = _list_of_questions.elementAt(_progress);
    setState(() {
      if (_progress == 0 || _permanent.length <= _progress) {
        _permanent.insert(_progress, question);
      } else {
        _permanent[_progress] = question;
        update_buttons_with_current_selections();
      }
      _permanent[_progress].selection_b = 1;
      _permanent[_progress].selection_a = 0;
      printit();
    });
  }

  void printit() {
    _my_text = _permanent.elementAt(_progress).selection_a.toString() +
        _permanent.elementAt(_progress).selection_b.toString();
  }

  void clearButs() {
    a_pressed = false;
    b_pressed = false;
  }

  void update_buttons_with_current_selections() {
    if (_permanent.elementAt(_progress).selection_a == 1) {
      a_pressed = true;
      b_pressed = false;
    } else {
      a_pressed = false;
      b_pressed = true;
    }
  }

  void calculate_intro_extro_perso() {
    //attempt to calculate E and I;
    Set<int> checking_set_e_i = ({1, 5, 9, 13, 17});
    int i;
    for (i = 0; i <= checking_set_e_i.length - 1; i++) {
      _result_e = _result_e +
          _permanent.elementAt(checking_set_e_i.elementAt(i)).selection_a;
    }
    for (i = 0; i <= checking_set_e_i.length - 1; i++) {
      _result_i = _result_i +
          _permanent.elementAt(checking_set_e_i.elementAt(i)).selection_b;
    }
    print('Extrovertive value: $_result_e');
    print('Extrovertive opositive value: $_result_i');
  }
}
