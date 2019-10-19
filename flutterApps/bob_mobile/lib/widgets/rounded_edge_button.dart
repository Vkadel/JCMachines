import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

Widget FormattedRoundedButton(String label, Function myFunction) {
  final String _label = label;
  final Function _myFunction = myFunction;
  return Container(
    margin: const EdgeInsets.fromLTRB(10, 20, 10, 10),
    child: OutlineButton(
      splashColor: Colors.grey,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(40),
      ),
      highlightElevation: 0,
      borderSide: BorderSide(color: Colors.grey),
      onPressed: _myFunction,
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 15, 0, 15),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(_label),
          ],
        ),
      ),
    ),
  );
}
