import 'package:json_annotation/json_annotation.dart';
part 'question.g.dart';

/// generated watcher in the root by running  command:
///
/// flutter pub run build_runner build
///
///               or for permanent
///
/// flutter pub run build_runner watch

@JsonSerializable(explicitToJson: true)
class Question {
  Question(this.question, this.option_a, this.option_b, this.selection_b,
      this.selection_a, this.id);

  String question;
  String option_a;
  String option_b;
  int selection_a;
  int selection_b;
  int id;

  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory Question.fromJson(Map<String, dynamic> json) =>
      _$QuestionFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$QuestionToJson(this);
}
