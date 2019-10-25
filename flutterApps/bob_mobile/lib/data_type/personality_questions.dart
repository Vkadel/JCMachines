import 'package:bob_mobile/data_type/question.dart';
import 'package:json_annotation/json_annotation.dart';
part 'personality_questions.g.dart';

@JsonSerializable(explicitToJson: true)
class PersonalityQuestions {
  PersonalityQuestions(this.questions);

  List<Question> questions;

  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory PersonalityQuestions.fromJson(Map<String, dynamic> json) =>
      _$PersonalityQuestionsFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$PersonalityQuestionsToJson(this);
}
