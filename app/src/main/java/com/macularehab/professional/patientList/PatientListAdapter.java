package com.macularehab.professional.patientList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.R;
import com.macularehab.exercises.Exercise;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.patient.Patient;
import com.macularehab.professional.patient.ProfessionalPatientHome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.PatientViewHolder> implements View.OnClickListener, Filterable {

    private LayoutInflater mInflater;
    private ArrayList<Patient> patientList;
    private ArrayList<Patient> patientListFull;
    private Context context;

    public PatientListAdapter(Context context, List<Patient> patientList) {

        this.context = context;
        mInflater = LayoutInflater.from(context);
        setPatientListData(patientList);
    }

    public void setPatientListData(List<Patient> patientList) {

        this.patientList = (ArrayList<Patient>) patientList;
        this.patientListFull = new ArrayList<>(patientList);
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

        String patient_uid = " ";
        patient_uid += patientList.get(position).getPatient_uid();
        String patient_name = " ";
        patient_name += patientList.get(position).getName();
        String patient_lastName = " ";
        patient_lastName += patientList.get(position).getFirst_lastName();
        String patient_dateLastTest = " ";
        String[] patient_dateLastTest_aux = patientList.get(position).getDate_last_test().split(" ");
        patient_dateLastTest = " " + patient_dateLastTest_aux[0].replaceAll("_", "/");

        Drawable progressDrawable = holder.card_patient_progress_bar.getProgressDrawable().mutate();

        Exercise exercise = patientList.get(position).getExercise();
        int exercises_completed = exercise.getExercises_completed();
        int num_exercises = exercise.getNum_exercises();

        int progress = exercises_completed*100/num_exercises;

        if (progress > 100) {
            progress = 100;
        }

        holder.card_patient_progress_bar.setProgress(progress);
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
        holder.card_patient_dateLastTest.setText(patient_dateLastTest);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public Filter getFilter() {
        return patientListFiltered;
    }

    private Filter patientListFiltered = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Patient> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(patientListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Patient patient : patientListFull) {

                    String name = patient.getName();
                    String first_lastName = patient.getFirst_lastName();
                    String second_lastName = patient.getSecond_lastName();

                    if (name != null && name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                    else if (first_lastName != null && first_lastName.toLowerCase().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                    else if (second_lastName != null && second_lastName.toLowerCase().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            patientList.clear();
            patientList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView card_patient_uid;
        public TextView card_patient_name;
        public TextView card_patient_last_name;
        public TextView card_patient_dateLastTest;
        private ProgressBar card_patient_progress_bar;
        private PatientListAdapter patientListAdapter;

        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;
        private FirebaseAuth mAuth;
        private final String db_professional = "Professional";
        private final String db_patients = "Patients";
        private final String filenameCurrentPatient = "CurrentPatient.json";

        public PatientViewHolder(@NonNull View itemView, PatientListAdapter patientListAdapter) {
            super(itemView);

            card_patient_uid = itemView.findViewById(R.id.professional_patient_info_card_view_patient_uid);
            card_patient_name = itemView.findViewById(R.id.professional_patient_info_card_view_patient_name);
            card_patient_last_name = itemView.findViewById(R.id.professional_patient_info_card_view_patient_last_name);
            card_patient_dateLastTest = itemView.findViewById(R.id.professional_patient_info_card_view_patient_dateLastTest);
            card_patient_progress_bar = itemView.findViewById(R.id.professional_patient_info_card_view_patient_progress_bar);

            this.patientListAdapter = patientListAdapter;

            //TODO
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPatientAndStartActivity(card_patient_uid.getText().toString().trim());
                }
            });
        }

        @Override
        public void onClick(View v) {

        }

        private void downloadPatientAndStartActivity(String patient_uid) {

            firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();

            downloadPatient(patient_uid);
        }

        private void downloadPatient(String patient_uid) {

            databaseReference.child(db_professional).child(mAuth.getUid()).child(db_patients).child(patient_uid)
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {

                        HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();

                        if (map == null) {
                            map = new HashMap<>();
                        }
                        writeInternalStoragePatientInfo(map);
                    }
                }
            });
        }

        private void writeInternalStoragePatientInfo(HashMap<String, Object> map) {

            Gson gson = new Gson();
            String data = gson.toJson(map);

            WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
            writeInternalStorage.write(context, filenameCurrentPatient, data);

            startNewActivity();
        }

        private void startNewActivity() {

            Intent intent = new Intent(context, ProfessionalPatientHome.class);
            context.startActivity(intent);
        }
    }
}
