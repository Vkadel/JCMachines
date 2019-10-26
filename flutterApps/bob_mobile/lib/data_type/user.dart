import 'package:json_annotation/json_annotation.dart';
part 'user.g.dart';

/// An annotation for the code generator to know that this class needs the
/// JSON serialization logic to be generated.
/// generated watcher in the root by running  command:
///
/// flutter pub run build_runner build
///
///               or for permanent
///
/// flutter pub run build_runner watch
///
///     of for when files already exists
///
/// flutter pub run build_runner build --delete-conflicting-outputs
///
/// Consuming json_serializable models.
///   Map userMap = jsonDecode(jsonString);
///   var user = User.fromJson(userMap);
///Encoding encoding. The calling API is the same as before.
///   String json = jsonEncode(user);

@JsonSerializable(explicitToJson: true)
class User {
  User(
    this.name,
    this.email,
    this.id,
    this.status,
    this.points,
    this.personality,
    this.role,
  );

  String name = 'Add a Name';
  String email;
  String id;
  int status; //0:offline 1: online
  int points;
  int role; //0:Undertermined 1:Knight 2:Mage 3: Archer
  Map<dynamic, dynamic>
      personality; //Will store all values of personality calculated
  String school_id;
  String team_id;
  List<int> answers;

  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$UserToJson(this);
}
