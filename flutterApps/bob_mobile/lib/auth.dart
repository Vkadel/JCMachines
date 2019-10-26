import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';

abstract class BaseAuth {
  Stream<FirebaseUser> get onAuthStateChanged;
  Future<String> signInWithEmailAndPassword(String email, String password);
  Future<String> createUserWithEmailAndPassword(String email, String password);
  Future<String> currentUser();
  Future<String> currentUserEmail();
  Future<String> signOut();
  Future<String> singInWithGoogle();
  Future<void> passwordReset(String email);
  FirebaseUser getLastUserLoged();
}

class Auth implements BaseAuth {
  final FirebaseAuth _firebaseAuth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn();
  FirebaseUser _user;

  @override
  Stream<FirebaseUser> get onAuthStateChanged =>
      _firebaseAuth.onAuthStateChanged.map((FirebaseUser user) => _user = user);

  @override
  Future<String> createUserWithEmailAndPassword(
      String email, String password) async {
    return (await _firebaseAuth.createUserWithEmailAndPassword(
            email: email, password: password))
        .user
        .uid;
  }

  @override
  Future<String> currentUser() async {
    return (await _firebaseAuth.currentUser()).uid;
  }

  @override
  Future<String> currentUserEmail() async {
    // TODO: implement currentUserEmail
    return (await _firebaseAuth.currentUser()).email;
  }

  @override
  Future<String> signInWithEmailAndPassword(
      String email, String password) async {
    return (await _firebaseAuth.signInWithEmailAndPassword(
            email: email, password: password))
        .user
        .uid;
  }

  @override
  Future<String> signOut() {
    //TODO: update online status will need to get user and change the status
    return (_firebaseAuth.signOut());
  }

  @override
  Future<String> singInWithGoogle() async {
    final GoogleSignInAccount account = await _googleSignIn.signIn();
    final GoogleSignInAuthentication _auth = await account.authentication;
    final AuthCredential credential = GoogleAuthProvider.getCredential(
        idToken: _auth.idToken, accessToken: _auth.accessToken);
    return (await _firebaseAuth.signInWithCredential(credential)).user.uid;
  }

  @override
  Future<void> passwordReset(String email) async {
    return (await _firebaseAuth.sendPasswordResetEmail(email: email));
  }

  @override
  FirebaseUser getLastUserLoged() {
    return _user;
  }
}
