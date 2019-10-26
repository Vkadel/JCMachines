import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class FormattedRoundedButtonSurveyGroup extends StatefulWidget {
  FormattedRoundedButtonSurveyGroup({Key key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return new _FormattedRoundedButtonSurveyGroupState();
  }
}

class _FormattedRoundedButtonSurveyGroupState
    extends State<FormattedRoundedButtonSurveyGroup> {
  List<bool> _pressed;
  List<String> _labels;
  List<Function> _funtions;

  @override
  Widget build(BuildContext context) {
    switch (_labels.length) {
      case 0:
        return null;
        break;
      case 1:
        return MyRoundedButton(_funtions.elementAt(0), _pressed.elementAt(0),
            _labels.elementAt(0));
        break;
      case 2:
        return Column(
          children: <Widget>[
            MyRoundedButton(
              _funtions.elementAt(0),
              _pressed.elementAt(0),
              _labels.elementAt(0),
            ),
            MyRoundedButton(_funtions.elementAt(1), _pressed.elementAt(0),
                _labels.elementAt(1)),
          ],
        );
        break;
      case 3:
        return Column(children: <Widget>[
          MyRoundedButton(
            _funtions.elementAt(0),
            _pressed.elementAt(0),
            _labels.elementAt(0),
          ),
          MyRoundedButton(_funtions.elementAt(1), _pressed.elementAt(1),
              _labels.elementAt(1)),
          MyRoundedButton(_funtions.elementAt(2), _pressed.elementAt(2),
              _labels.elementAt(2)),
        ]);
        break;
      case 4:
        return Column(children: <Widget>[
          MyRoundedButton(
            _funtions.elementAt(0),
            _pressed.elementAt(0),
            _labels.elementAt(0),
          ),
          MyRoundedButton(_funtions.elementAt(1), _pressed.elementAt(1),
              _labels.elementAt(1)),
          MyRoundedButton(_funtions.elementAt(2), _pressed.elementAt(2),
              _labels.elementAt(2)),
          MyRoundedButton(_funtions.elementAt(3), _pressed.elementAt(3),
              _labels.elementAt(3)),
        ]);
        break;
    }
  }
}

Widget MyRoundedButton(Function myFunction, bool pressed, String label) {
  if (pressed) {
    //Container if pressed
    return Container(
      margin: const EdgeInsets.fromLTRB(16, 10, 16, 10),
      child: RaisedButton(
        splashColor: Colors.orangeAccent,
        color: Colors.deepOrange,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(40),
        ),
        highlightElevation: 0,
        onPressed: () {
          print('Hi Really setting state');
          myFunction();
        },
        child: Padding(
          padding: const EdgeInsets.fromLTRB(8, 15, 8, 15),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Expanded(
                child: Text(
                  label,
                  maxLines: 15,
                  textAlign: TextAlign.center,
                  style: new TextStyle(
                      color: Colors.white, fontWeight: FontWeight.w500),
                ),
              )
            ],
          ),
        ),
      ),
    );
  } else {
    //Container if NOT pressed
    return Container(
      margin: const EdgeInsets.fromLTRB(16, 10, 16, 10),
      child: RaisedButton(
        splashColor: Colors.orangeAccent,
        color: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(40),
        ),
        highlightElevation: 0,
        onPressed: () {
          print('Hi Really setting state');
          myFunction();
        },
        child: Padding(
          padding: const EdgeInsets.fromLTRB(8, 15, 8, 15),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Expanded(
                child: Text(
                  label,
                  maxLines: 15,
                  textAlign: TextAlign.center,
                  style: new TextStyle(
                      color: Colors.deepOrange, fontWeight: FontWeight.w500),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
