import 'package:flutter/cupertino.dart';

//This Widget enables scrolling on a

class myScrollableWindow extends StatelessWidget {
  Widget _child;
  BuildContext _context;
  myScrollableWindow(BuildContext context, Widget child) {
    _context = context;
    _child = child;
  }
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return LayoutBuilder(
      builder: (BuildContext context, BoxConstraints viewportConstraints) {
        return SingleChildScrollView(
          child: ConstrainedBox(
            constraints: BoxConstraints(
              minHeight: viewportConstraints.maxHeight,
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                Container(
                  // A fixed-height child.
                  child: _child,
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
