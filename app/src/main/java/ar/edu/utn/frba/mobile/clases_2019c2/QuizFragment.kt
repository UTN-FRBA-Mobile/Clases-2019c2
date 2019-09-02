package ar.edu.utn.frba.mobile.clases_2019c2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_quiz.*


class QuizFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        botonSubmit.setOnClickListener{

            if (radio_si.isChecked){
                findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToWinFragment())

            }else if(radio_no.isChecked){
                findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToLoseFragment())
            }

        }

    }


}
