import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:bob_mobile/auth.dart';

Widget GoogleSingInButton(String label, Auth auth) {
  return Container(
    margin: const EdgeInsets.fromLTRB(10, 0, 10, 10),
    child: OutlineButton(
      splashColor: Colors.grey,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(40)),
      highlightElevation: 0,
      borderSide: BorderSide(color: Colors.grey),
      onPressed: () async {
        try {
          final _auth = auth;
          final id = await _auth.singInWithGoogle();
          print('Signed in with Google $id');
        } catch (e) {
          print(e);
        }
      },
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Image(
              image: AssetImage('assets/google_logo.png'),
              width: 25,
              height: 25,
            ),
            Text('   $label'),
          ],
        ),
      ),
    ),
  );
}
