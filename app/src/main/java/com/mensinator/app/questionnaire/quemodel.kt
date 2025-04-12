package com.mensinator.app.questionnaire

import com.google.android.gms.common.util.CollectionUtils.listOf
import java.util.ArrayList

object Constants {

    const val USER_NAME: String = "user_name"

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        val q1 = Question(
            1,
            "What property of aluminium makes it ideal for use in heat-dissipating applications?",
            0,
            listOf("Good Thermal Conductivity", "High electrical resistance", "high melting point", "low density")
        )
        questionsList.add(q1)

        val q2 = Question(
            2,
            "What characteristic of boron is linked to its ability to form covalent bonds?",
            0,
            listOf("excellent conductor of electricity", "poor conductor of electricity", "forms ionic bonds", "has a metallic luster")
        )
        questionsList.add(q2)

        val q3 = Question(
            3,
            "Which material is primarily used in pencils due to its carbon content?",
            0,
            listOf("Graphite", "Clay", "Plastic", "Wood")
        )
        questionsList.add(q3)

        val q4 = Question(
            4,
            "What is the primary purpose of adding fluoride to public water supplies?",
            0,
            listOf("To improve water taste", "To increase water hardness", "To reduce the incidence of cavities", "to remove contaminants from water")
        )
        questionsList.add(q4)

        val q5 = Question(
            5,
            "Which property of lead allows it to be easily shaped into sheets or other forms?",
            0,
            listOf("Conductivity", "Malleability", "Brittleness", "Rigidity")
        )
        questionsList.add(q5)

        val q6 = Question(
            6,
            "What are the sensory characteristics of oxygen under standard conditions?",
            0,
            listOf("Colorless, with a distinct odor and taste", "Greenish, with a strong smell", "Blue, odorless, and tasteless", "Colorless, odorless, and tasteless")
        )
        questionsList.add(q6)

        val q7 = Question(
            7,
            "Which of the following characteristics is true for sodium?",
            0,
            listOf("It remains stable in water", "It reacts explosively with water", "It forms a protective oxide layer", "It is used in jewelry due to its luster")
        )
        questionsList.add(q7)

        val q8 = Question(
            8,
            "What is the typical color of elemental sulfur in its natural form?",
            0,
            listOf("Red", "Black", "Dark Green", "Bright Yellow")
        )
        questionsList.add(q8)

        val q9 = Question(
            9,
            "What is notable about xenon compared to other noble gases?",
            0,
            listOf("Lightest noble gas", "reactive noble gas", "heaviest noble gas", "least dense noble gas")
        )
        questionsList.add(q9)

        val q10 = Question(
            10,
            "What does it mean for neon to be chemically inert?",
            0,
            listOf("It does not react with other elements or compounds", "It reacts readily with other elements", "It forms compounds with most other gases", "It is highly reactive and combustible")
        )
        questionsList.add(q10)

        return questionsList
    }
}
