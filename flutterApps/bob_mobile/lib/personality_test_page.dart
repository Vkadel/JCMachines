import 'package:bob_mobile/data_type/question.dart';
import 'package:bob_mobile/data_type/user.dart';
import 'package:bob_mobile/widgets/rounded_edge_button_survey.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'constants.dart';
import 'provider.dart';
import 'qanda.dart';

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
  List<Question> _list_of_questions;
  String _mytext = '';

  @override
  void initState() {}

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
              var question = _list_of_questions
                  .elementAt(Quanda.of(context).progress)
                  .option_a;
              print('Test of question Received: $question');
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
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: <Widget>[
          Container(
            alignment: Alignment.topCenter,
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 10),
            child: Text(
              _list_of_questions
                  .elementAt(Quanda.of(this.context).progress)
                  .question,
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 30, fontWeight: FontWeight.w200),
            ),
          ),
          MyRoundedButton(
            press_a,
            Quanda.of(context).a_pressed,
            _list_of_questions.elementAt(Quanda.of(context).progress).option_a,
          ),
          MyRoundedButton(
            press_b,
            Quanda.of(context).b_pressed,
            _list_of_questions.elementAt(Quanda.of(context).progress).option_b,
          ),
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
                Text('$_mytext'),
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
    int showing = Quanda.of(context).progress;
    print('Trying to change state from: $showing');
    setState(() {
      if (Quanda.of(context).progress ==
          Constants().number_of_questions_personality_test) {
        Quanda.of(context).progress = 0;
        //TODO: Make sure only one check if done and only happens when
        //When permanent is size 20. Then it pops back into the dashboard.
        return;
      } else {
        if (Quanda.of(context).progress ==
            Constants().number_of_questions_personality_test - 1) {
          //Calculate Personality
          calculate_intro_extro_perso();
        }
        if (Quanda.of(context).progress <
            Constants().number_of_questions_personality_test - 1) {
          print('Trying to change state from: $showing');
          Quanda.of(context).progress = Quanda.of(context).progress + 1;
        }
      }
      if (Quanda.of(context).progress < Quanda.of(context).permanent.length) {
        update_buttons_with_current_selections();
      }
    });
  }

  void Go_prev() {
    clearButs();
    setState(() {
      if (Quanda.of(context).progress == 0) {
        Quanda.of(context).progress =
            Constants().number_of_questions_personality_test - 1;
      } else {
        if (Quanda.of(context).progress > 0) {
          print('Trying to change state');
          Quanda.of(context).progress = Quanda.of(context).progress - 1;
          //Clean last Item
        }
      }
      update_buttons_with_current_selections();
    });
  }

  void press_a() {
    print('setting state to A');
    Quanda.of(context).b_pressed = false;
    Quanda.of(context).a_pressed = true;
    Question question =
        _list_of_questions.elementAt(Quanda.of(context).progress);
    setState(() {
      if (Quanda.of(context).progress == 0 ||
          Quanda.of(context).permanent.length <= Quanda.of(context).progress) {
        Quanda.of(context)
            .permanent
            .insert(Quanda.of(context).progress, question);
      } else {
        Quanda.of(context).permanent[Quanda.of(context).progress] = question;
        update_buttons_with_current_selections();
      }
      Quanda.of(context).permanent[Quanda.of(context).progress].selection_b = 0;
      Quanda.of(context).permanent[Quanda.of(context).progress].selection_a = 1;
      printit();
    });
  }

  void press_b() {
    print('setting state to B');
    Quanda.of(context).b_pressed = true;
    Quanda.of(context).a_pressed = false;
    Question question =
        _list_of_questions.elementAt(Quanda.of(context).progress);
    setState(() {
      if (Quanda.of(context).progress == 0 ||
          Quanda.of(context).permanent.length <= Quanda.of(context).progress) {
        Quanda.of(context)
            .permanent
            .insert(Quanda.of(context).progress, question);
      } else {
        Quanda.of(context).permanent[Quanda.of(context).progress] = question;
        update_buttons_with_current_selections();
      }
      Quanda.of(context).permanent[Quanda.of(context).progress].selection_b = 1;
      Quanda.of(context).permanent[Quanda.of(context).progress].selection_a = 0;
      printit();
    });
  }

  void printit() {
    _mytext = Quanda.of(context)
            .permanent
            .elementAt(Quanda.of(context).progress)
            .selection_a
            .toString() +
        Quanda.of(context)
            .permanent
            .elementAt(Quanda.of(context).progress)
            .selection_b
            .toString();
  }

  void clearButs() {
    Quanda.of(context).a_pressed = false;
    Quanda.of(context).b_pressed = false;
  }

  void update_buttons_with_current_selections() {
    if (Quanda.of(context)
            .permanent
            .elementAt(Quanda.of(context).progress)
            .selection_a ==
        1) {
      Quanda.of(context).a_pressed = true;
      Quanda.of(context).b_pressed = false;
    } else {
      Quanda.of(context).a_pressed = false;
      Quanda.of(context).b_pressed = true;
    }
  }

  void calculate_intro_extro_perso() {
    //Calculate E and I;
    Set<int> checking_set_e_i = ({1, 5, 9, 13, 17});
    Set<int> checking_set_s_n = ({2, 6, 10, 14, 18});
    Set<int> checking_set_t_f = ({3, 7, 11, 15, 19});
    Set<int> checking_set_j_p = ({4, 8, 12, 16, 19});
    int i = 0;

    for (i = 0; i <= checking_set_e_i.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_e'] =
          Quanda.of(context).myUser.personality['value_e'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_e_i.elementAt(i))
                  .first
                  .selection_a;
    }

    for (i = 0; i <= checking_set_e_i.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_i'] =
          Quanda.of(context).myUser.personality['value_i'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_e_i.elementAt(i))
                  .first
                  .selection_b;
    }

    for (i = 0; i <= checking_set_s_n.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_s'] =
          Quanda.of(context).myUser.personality['value_s'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_s_n.elementAt(i))
                  .first
                  .selection_a;
    }

    for (i = 0; i <= checking_set_s_n.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_n'] =
          Quanda.of(context).myUser.personality['value_n'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_s_n.elementAt(i))
                  .first
                  .selection_b;
    }

    for (i = 0; i <= checking_set_t_f.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_t'] =
          Quanda.of(context).myUser.personality['value_t'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_t_f.elementAt(i))
                  .first
                  .selection_a;
    }

    for (i = 0; i <= checking_set_t_f.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_f'] =
          Quanda.of(context).myUser.personality['value_f'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_t_f.elementAt(i))
                  .first
                  .selection_b;
    }

    for (i = 0; i <= checking_set_j_p.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_j'] =
          Quanda.of(context).myUser.personality['value_j'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_j_p.elementAt(i))
                  .first
                  .selection_a;
    }

    for (i = 0; i <= checking_set_j_p.length - 1; i++) {
      Quanda.of(context).myUser.personality['value_p'] =
          Quanda.of(context).myUser.personality['value_p'] +
              Quanda.of(context)
                  .permanent
                  .where((l) => l.id == checking_set_j_p.elementAt(i))
                  .first
                  .selection_b;
    }
    //TODO: Upload Personality to server
    int intro = Quanda.of(context).myUser.personality['value_i'];
    int extro = Quanda.of(context).myUser.personality['value_e'];
    int S = Quanda.of(context).myUser.personality['value_s'];
    int N = Quanda.of(context).myUser.personality['value_n'];
    int T = Quanda.of(context).myUser.personality['value_t'];
    int F = Quanda.of(context).myUser.personality['value_f'];
    int J = Quanda.of(context).myUser.personality['value_j'];
    int P = Quanda.of(context).myUser.personality['value_p'];

    print('Personality Values: E:$extro I:$intro S:'
        '$S N:$N T:$T F:$F J:$J P:$P');

    Provider.of(context).fireBase.setUpUserPersonality(
        Provider.of(context).auth.getLastUserLoged(),
        Quanda.of(context).myUser);
  }
}
