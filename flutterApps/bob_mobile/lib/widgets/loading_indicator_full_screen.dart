import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget mLoadingIndicatorFullScreen(
    BuildContext context, ConnectionState state) {
  return Container(
    child: Center(child: CircularProgressIndicator()),
  );
}
