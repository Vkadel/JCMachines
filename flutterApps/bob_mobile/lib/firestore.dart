import 'package:cloud_firestore/cloud_firestore.dart';

abstract class BoBFireBase {
  Stream<QuerySnapshot> get_userprofile(String uid);
  Future<void> UserExist(String uid);
}

class MBobFireBase implements BoBFireBase {
  final Firestore _firestore = Firestore.instance;

  @override
  Future<void> UserExist(String uid) {
    // TODO: implement persistance enabled
    get_userprofile(uid)
        .listen((data) => data.documents.forEach((doc) => print(doc["uid"])));
  }

  @override
  // ignore: non_constant_identifier_names
  Stream<QuerySnapshot> get_userprofile(String uid) {
    print('SNAPSHOT Connection: looking for $uid');
    return (_firestore
        .collection('users')
        .where('uid', isEqualTo: uid)
        .snapshots());
  }
}
