import 'dart:convert';

import 'package:bob_mobile/data_type/personality_questions.dart';
import 'package:bob_mobile/data_type/user.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

abstract class BoBFireBase {
  Stream<QuerySnapshot> get_userprofile(String uid);
  Future<void> UserExist(String uid);
  Future<void> createUserProfile(String uid, String email);
  Stream<QuerySnapshot> getQuestions();
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
        .where('id', isEqualTo: uid)
        .snapshots());
  }

  @override
  Future<void> createUserProfile(String uid, String email) {
    //TODO: String
    var user = new User('Enter New Name', email, uid, 1, 0, 0);
    _firestore.collection('users').document().setData(user.toJson());
  }

  @override
  Stream<QuerySnapshot> getQuestions() {
    // TODO: String
    return _firestore.collection('personality_survey_q').snapshots();
  }
}
