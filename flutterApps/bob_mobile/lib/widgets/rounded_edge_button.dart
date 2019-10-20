import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget FormattedRoundedButton(String label, Function myFunction) {
  final String _label = label;
  final Function _myFunction = myFunction;
  return Container(
    margin: const EdgeInsets.fromLTRB(10, 20, 10, 10),
    child: RaisedButton(
      splashColor: Colors.orangeAccent,
      color: Colors.deepOrange,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(40),
      ),
      highlightElevation: 0,
      onPressed: _myFunction,
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 15, 0, 15),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              _label,
              style: new TextStyle(color: Colors.white),
            ),
          ],
        ),
      ),
    ),
  );
}
