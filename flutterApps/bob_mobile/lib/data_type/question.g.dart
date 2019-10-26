// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'question.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Question _$QuestionFromJson(Map<String, dynamic> json) {
  return Question(
      json['question'] as String,
      json['option_a'] as String,
      json['option_b'] as String,
      json['selection_b'] as int,
      json['selection_a'] as int,
      json['id'] as int);
}

Map<String, dynamic> _$QuestionToJson(Question instance) => <String, dynamic>{
      'question': instance.question,
      'option_a': instance.option_a,
      'option_b': instance.option_b,
      'selection_a': instance.selection_a,
      'selection_b': instance.selection_b,
      'id': instance.id
    };
