package com.mensinator.app.questionnaire

object Constants {

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // Q1 - Name (short answer)
        questionsList.add(
            Question(
                id = 1,
                question = "What's your name?",
                type = QuestionType.SHORT_ANSWER
            )
        )

        // Q2 - Last period date (calendar input)
        questionsList.add(
            Question(
                id = 2,
                question = "When did your last period start?",
                type = QuestionType.DATE
            )
        )

        // Q3 - Period length (dropdown)
        questionsList.add(
            Question(
                id = 3,
                question = "How long does your period usually last?",
                type = QuestionType.SHORT_ANSWER,
            )
        )

        // Q4 - Average menstrual cycle length (dropdown)
        questionsList.add(
            Question(
                id = 4,
                question = "What is your average menstrual cycle length (from one period to the next)?",
                type = QuestionType.SHORT_ANSWER,
            )
        )

        // Q5 - Menstrual cramps (multiple choice)
        questionsList.add(
            Question(
                id = 5,
                question = "Do you experience menstrual cramps?",
                type = QuestionType.MULTIPLE_CHOICE,
                options = listOf("None", "Mild", "Moderate", "Severe")
            )
        )
        // Q8 - Mood swings (yes/no)
        questionsList.add(
            Question(
                id = 6,
                question = "Do you experience mood swings before or during your period?",
                type = QuestionType.MULTIPLE_CHOICE,
                options = listOf("Yes", "No")
            )
        )

        // Q6 - Diagnosed conditions (short answer)
        questionsList.add(
            Question(
                id = 7,
                question = "Have you ever been diagnosed with any reproductive health conditions? (e.g., PCOS, Endometriosis, Fibroids, etc.)",
                type = QuestionType.SHORT_ANSWER
            )
        )

        // Q7 - Medication related to menstrual/hormonal health (yes/no + specify)
        questionsList.add(
            Question(
                id = 8,
                question = "Are you currently taking any medication related to your menstrual or hormonal health? If yes, please specify.",
                type = QuestionType.SHORT_ANSWER
            )
        )

        // Q8 - Mood swings (yes/no)
        questionsList.add(
            Question(
                id = 9,
                question = "Do you experience mood swings before or during your period?",
                type = QuestionType.MULTIPLE_CHOICE,
                options = listOf("Yes", "No")
            )
        )

        return questionsList
    }
}
