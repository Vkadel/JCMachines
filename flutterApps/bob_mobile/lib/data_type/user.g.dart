// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

User _$UserFromJson(Map<String, dynamic> json) {
  return User(
      json['name'] as String,
      json['email'] as String,
      json['id'] as String,
      json['status'] as int,
      json['points'] as int,
      json['personality'] as Map<dynamic, dynamic>,
      json['role'] as int)
    ..school_id = json['school_id'] as String
    ..team_id = json['team_id'] as String
    ..answers = (json['answers'] as List)?.map((e) => e as int)?.toList();
}

Map<String, dynamic> _$UserToJson(User instance) => <String, dynamic>{
      'name': instance.name,
      'email': instance.email,
      'id': instance.id,
      'status': instance.status,
      'points': instance.points,
      'role': instance.role,
      'personality': instance.personality,
      'school_id': instance.school_id,
      'team_id': instance.team_id,
      'answers': instance.answers
    };
