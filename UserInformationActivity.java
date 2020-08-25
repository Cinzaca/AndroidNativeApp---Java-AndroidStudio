package com.example.holdmycards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformationActivity extends AppCompatActivity {
    ImageView art4, logoutIconUser;
    static TextView dashboardText, subDashboardText, numeUtilizator, numeCard, numarCard, dataExpirarii, textDataExpirarii, logoutTextUser;
    View creditCard;
    Button changeData, unhideData;
    EditText completeNameField;
    CircleImageView uploadedPictureCard;
    Animation fromTop, fromRight, fromLeft;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseStorage storage;
    //String userID;
    //File localFile = File.createTempFile(MainActivity.userID, "jpg");;


    String SHARED_PREFS = "sharedPrefs";
    String SHARED_PREFS2 = "sharedPrefs2";
    String SHARED_PREFS3 = "sharedPrefs3";
    String SHARED_PREFS4 = "sharedPrefs4";
    String userCompleteName = "";
    String getUserCompleteName;
    String cardName = "";
    String getCardName;
    String cardNumber = "";
    String getCardNumber;
    String expirationDate = "";
    String getExpirationDate;
    StringBuilder sbCardNumber = new StringBuilder();
    BankActivity b = new BankActivity();
    final Handler handler = new Handler();

    public UserInformationActivity() throws IOException {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        art4 = (ImageView) findViewById(R.id.art4);
        dashboardText = (TextView) findViewById(R.id.dashboard);
        subDashboardText = (TextView) findViewById(R.id.subDashboard);
        creditCard = (View) findViewById(R.id.creditCard);
        changeData = (Button) findViewById(R.id.modifyTheCard);
        numeUtilizator = (TextView) findViewById(R.id.numeUtilizator);
        numeCard = (TextView) findViewById(R.id.numeCard);
        completeNameField = (EditText) findViewById(R.id.completeNameBar);
        numarCard = (TextView) findViewById(R.id.numarCard);
        dataExpirarii = (TextView) findViewById(R.id.dataExpirarii);
        textDataExpirarii = (TextView) findViewById(R.id.textDataExpirarii);
        uploadedPictureCard = (CircleImageView) findViewById(R.id.uploadedPictureCard);
        logoutIconUser = (ImageView) findViewById(R.id.logoutIconUser);
        logoutTextUser = (TextView) findViewById(R.id.logoutTextUser);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        //userID = fAuth.getCurrentUser().getUid();
        unhideData = (Button) findViewById(R.id.unhideTheData);


        fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        fromLeft = AnimationUtils.loadAnimation(this, R.anim.fromleft);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.fromright);

        art4.startAnimation(fromTop);
        dashboardText.startAnimation(fromLeft);
        subDashboardText.startAnimation(fromRight);
        creditCard.startAnimation(fromLeft);
        changeData.startAnimation(fromRight);
        numeUtilizator.startAnimation(fromLeft);
        numeCard.startAnimation(fromLeft);
        numarCard.startAnimation(fromLeft);
        dataExpirarii.startAnimation(fromLeft);
        textDataExpirarii.startAnimation(fromLeft);
        logoutIconUser.startAnimation(fromLeft);
        logoutTextUser.startAnimation(fromLeft);
        uploadedPictureCard.startAnimation(fromLeft);
        unhideData.startAnimation(fromLeft);




        unhideData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*numeUtilizator.setVisibility(View.VISIBLE);
                numeCard.setVisibility(View.VISIBLE);
                numarCard.setVisibility(View.VISIBLE);
                dataExpirarii.setVisibility(View.VISIBLE);
                textDataExpirarii.setVisibility(View.VISIBLE);
                downloadImage();
                //changeImage();
                uploadedPictureCard.setVisibility(View.VISIBLE);*/
                Toast.makeText(UserInformationActivity.this, "Not yet available!", Toast.LENGTH_SHORT).show();
            }
        });

        logoutIconUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserInformationActivity.this, "Signing out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        uploadedPictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserInformationActivity.this, "Not implemented!", Toast.LENGTH_SHORT).show();
            }
        });


        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBankActivity();
            }
        });

        loadAndUploadData();
    }


    public void loadAndUploadData() {
        loadData();
        updateData();
        loadData2();
        updateData2();
        loadData3();
        updateData3();
        loadData4();
        updateData4();
    }

    public void openBankActivity() {
        Intent intent = new Intent(this, BankActivity.class);
        startActivity(intent);
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        getUserCompleteName = sharedPreferences.getString(userCompleteName, "");
    }

    public void loadData2() {
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        getCardName = sharedPreferences2.getString(cardName, "");
    }

    public void loadData3() {
        SharedPreferences sharedPreferences3 = getSharedPreferences(SHARED_PREFS3, MODE_PRIVATE);
        getCardNumber = sharedPreferences3.getString(cardNumber, this.sbCardNumber.toString());
    }

    public void loadData4() {
        SharedPreferences sharedPreferences4 = getSharedPreferences(SHARED_PREFS4, MODE_PRIVATE);
        getExpirationDate = sharedPreferences4.getString(expirationDate, "");
    }

    public void updateData() {
        numeUtilizator.setText(getUserCompleteName);
    }

    public void updateData2() {
        numeCard.setText(getCardName);
    }

    public void updateData3() {
        char[] cardNumberArray = getCardNumber.toCharArray();
        for (int i = 0; i < cardNumberArray.length; i++) {
            if (i % 4 == 0 && i != 0) {
                this.sbCardNumber.append(' ');
            }
            this.sbCardNumber.append(cardNumberArray[i]);
        }

        numarCard.setText(this.sbCardNumber);
    }

    public void updateData4() {
        StringBuilder sb = new StringBuilder();
        char[] expirationDateArray = getExpirationDate.toCharArray();
        for (int i = 0; i < expirationDateArray.length; i++) {
            if (i % 2 == 0 && i != 0) {
                sb.append('/');
            }
            sb.append(expirationDateArray[i]);
        }
        dataExpirarii.setText(sb);
    }

    //In progress
    /*
    public void changeImage(){
            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
            uploadedPictureCard.setImageBitmap(myBitmap);
    }*/



    //In progress
    /*

    public void downloadImage(){
        StorageReference ref = storageReference.child(MainActivity.userID + "JPG");
        ref.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UserInformationActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UserInformationActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                // Handle failed download
                // ...
            }
        });
    }*/
}
