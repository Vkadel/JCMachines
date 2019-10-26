// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'personality_questions.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PersonalityQuestions _$PersonalityQuestionsFromJson(Map<String, dynamic> json) {
  return PersonalityQuestions((json['questions'] as List)
      ?.map((e) =>
          e == null ? null : Question.fromJson(e as Map<String, dynamic>))
      ?.toList());
}

Map<String, dynamic> _$PersonalityQuestionsToJson(
        PersonalityQuestions instance) =>
    <String, dynamic>{
      'questions': instance.questions?.map((e) => e?.toJson())?.toList()
    };
