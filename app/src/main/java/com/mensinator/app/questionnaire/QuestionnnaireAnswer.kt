package com.mensinator.app.questionnaire

data class QuestionnaireAnswer(
    val questionId: Int,
    val questionText: String,
    val answer: String,
    val questionType: QuestionType
) {
    companion object {
        fun fromMap(
            answers: Map<Int, String>,
            questions: List<Question>
        ): List<QuestionnaireAnswer> {
            return questions.map { q ->
                QuestionnaireAnswer(
                    questionId = q.id,
                    questionText = q.question,
                    answer = answers[q.id] ?: "Not answered",
                    questionType = q.type
                )
            }
        }
    }
}