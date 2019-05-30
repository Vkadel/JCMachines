package com.example.virginia.jcmachines.utils;

import android.content.Context;
import android.content.Intent;

import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.R;
import com.google.firebase.auth.FirebaseAuth;

public class SendMyEmail {
    String myEmailText;
    public SendMyEmail(effcalculation mycalc, Context context){

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String msg="";
        //Generate Message
        if(mycalc.getImp()){
            //Construct imperial msg
            msg=context.getResources().getString(R.string.efficiency)+mycalc.getEff()+"\n";
            msg=msg+context.getResources().getString(R.string.type_of_machine)+mycalc.getMid()+"\n";
            msg=msg+context.getResources().getString(R.string.column_speed_imp)+mycalc.getV()+"\n";
            msg=msg+context.getResources().getString(R.string.cut_length_imp)+mycalc.getC()+"\n";
            msg=msg+context.getResources().getString(R.string.cut_rate_imp)+mycalc.getL()+"\n";
            msg=msg+context.getResources().getString(R.string.estimation_of_waste)+mycalc.getR()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_material_area_imp)+mycalc.getAx()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_brick_w_imp)+mycalc.getW()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_brick_h_imp)+mycalc.getH()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_void_percentage)+mycalc.getZ()+"\n";
            msg=msg+context.getResources().getString(R.string.auger_speed_imp)+mycalc.getN()+"\n";
            msg=msg+context.getResources().getString(R.string.theoretical_volume_displacement)+mycalc.getVd()+"\n";
        }else{
            msg=msg+context.getResources().getString(R.string.type_of_machine)+mycalc.getMid()+"\n";
            msg=context.getResources().getString(R.string.efficiency)+mycalc.getEff()+"\n";
            msg=msg+context.getResources().getString(R.string.column_speed_metric)+mycalc.getV()+"\n";
            msg=msg+context.getResources().getString(R.string.cut_length_metric)+mycalc.getC()+"\n";
            msg=msg+context.getResources().getString(R.string.cut_rate_metric)+mycalc.getL()+"\n";
            msg=msg+context.getResources().getString(R.string.estimation_of_waste)+mycalc.getR()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_material_area_metric)+mycalc.getAx()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_brick_w_metric)+mycalc.getW()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_brick_h_metric)+mycalc.getH()+"\n";
            msg=msg+context.getResources().getString(R.string.enter_void_percentage)+mycalc.getZ()+"\n";
            msg=msg+context.getResources().getString(R.string.auger_speed_metric)+mycalc.getN()+"\n";
            msg=msg+context.getResources().getString(R.string.theoretical_volume_displacement)+mycalc.getVd()+"\n";
        }
        String to=context.getResources().getString(R.string.email_addresss_from_cc_calculations);
        //Create Intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.subject_efficiency_email)+userName);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {to});
        context.startActivity(Intent.createChooser(intent, "Share Link"));
    }
}
