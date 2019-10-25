import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget FormattedRoundedButtonSurvey(
    String label, Function myFunction, bool pressed) {
  final String _label = label;
  final Function _myFunction = myFunction;
  if (pressed) {
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
          _myFunction();
        },
        child: Padding(
          padding: const EdgeInsets.fromLTRB(8, 15, 8, 15),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Expanded(
                child: Text(
                  _label,
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
          _myFunction();
        },
        child: Padding(
          padding: const EdgeInsets.fromLTRB(8, 15, 8, 15),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Expanded(
                child: Text(
                  _label,
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
