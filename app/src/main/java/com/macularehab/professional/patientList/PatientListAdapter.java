package com.macularehab.professional.patientList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macularehab.R;
import com.macularehab.patient.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.PatientViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private ArrayList<Patient> patientList;
    private Context context;

    public PatientListAdapter(Context context, List<Patient> patientList) {

        this.context = context;
        mInflater = LayoutInflater.from(context);
        setPatientListData(patientList);
    }

    public void setPatientListData(List<Patient> patientList) {

        this.patientList = (ArrayList<Patient>) patientList;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.recycler_view_patient_information, parent, false);
        return new PatientViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {

        String patient_uid = "Patient: ";
        patient_uid = patientList.get(position).getPatient_uid();
        String patient_name = "Name: ";
        patient_name = patientList.get(position).getName();
        String patient_lastName = "Last Name: ";
        patient_lastName = patientList.get(position).getName();

        Drawable progressDrawable = holder.card_patient_progress_bar.getProgressDrawable().mutate();
        holder.card_patient_progress_bar.setProgress((position*25) + 25);
        int progress = holder.card_patient_progress_bar.getProgress();
        if (progress <= 30) {
            progressDrawable.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 60) {
            progressDrawable.setColorFilter(Color.parseColor("#ADD8E6"), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 80) {
            progressDrawable.setColorFilter(Color.parseColor("#90EE90"), PorterDuff.Mode.SRC_IN);
        }
        else {
            progressDrawable.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_IN);
        }

        holder.card_patient_progress_bar.setProgressDrawable(progressDrawable);

        holder.card_patient_uid.setText(patient_uid);
        holder.card_patient_name.setText(patient_name);
        holder.card_patient_last_name.setText(patient_lastName);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView card_patient_uid;
        public TextView card_patient_name;
        public TextView card_patient_last_name;
        private ProgressBar card_patient_progress_bar;
        private PatientListAdapter patientListAdapter;

        public PatientViewHolder(@NonNull View itemView, PatientListAdapter patientListAdapter) {
            super(itemView);

            card_patient_uid = itemView.findViewById(R.id.professional_patient_info_card_view_patient_uid);
            card_patient_name = itemView.findViewById(R.id.professional_patient_info_card_view_patient_name);
            card_patient_last_name = itemView.findViewById(R.id.professional_patient_info_card_view_patient_last_name);
            card_patient_progress_bar = itemView.findViewById(R.id.professional_patient_info_card_view_patient_progress_bar);

            this.patientListAdapter = patientListAdapter;

            //TODO
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
