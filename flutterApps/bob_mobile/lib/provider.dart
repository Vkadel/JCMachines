import 'package:bob_mobile/auth.dart';
import 'package:flutter/material.dart';

class Provider extends InheritedWidget {
  final BaseAuth auth;

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) {
    return true;
  }

  Provider({Key key, Widget child, this.auth})
      : super(
          key: key,
          child: child,
        );

  static Provider of(BuildContext context) =>
      (context.inheritFromWidgetOfExactType(Provider) as Provider);
}
