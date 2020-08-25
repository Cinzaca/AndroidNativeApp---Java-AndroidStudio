package com.example.holdmycards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class BankActivity extends AppCompatActivity {
    private static final String TAG = null;
    ImageView art, completeNameIcon, usernameIcon2, cardNumberIcon, expirationDateIcon, logoutIcon;
    View view;
    EditText completeNameField, cardNameField, cardNumberField, expirationDateField;
    TextView title, subtitle, logoutText;
    Button viewCardButton, saveProfileButton;
    CircleImageView uploadedPicture;
    Animation fromBottom2, fromRight, fromLeft, fromBottom3;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userID;
    File localFile;


    //variables for datas
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

    private int STORAGE_PERMISSION_CODE = 1;

    private static final int GALLERY_REQUEST_CODE = 123;
    public Uri imageData;

    public BankActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        art = (ImageView) findViewById(R.id.art3);
        view = (View) findViewById(R.id.view);
        completeNameField = (EditText) findViewById(R.id.completeNameBar);
        completeNameIcon = (ImageView) findViewById(R.id.completenameicon);
        cardNameField = (EditText) findViewById(R.id.cardNameBar);
        usernameIcon2 = (ImageView) findViewById(R.id.usernameicon3);
        cardNumberField = (EditText) findViewById(R.id.cardNumberField);
        cardNumberIcon = (ImageView) findViewById(R.id.creditCardIcon);
        expirationDateField = (EditText) findViewById(R.id.expirationDateField);
        expirationDateIcon = (ImageView) findViewById(R.id.expirationDateIcon);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        uploadedPicture = (CircleImageView) findViewById(R.id.uploadedPicture);
        viewCardButton = (Button) findViewById(R.id.viewCardButton);
        saveProfileButton = (Button) findViewById(R.id.saveProfileButton);
        logoutIcon = (ImageView) findViewById(R.id.logoutIconBank);
        logoutText = (TextView) findViewById(R.id.logoutTextBank);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        fromBottom3 = AnimationUtils.loadAnimation(this, R.anim.frombottom3);
        fromBottom2 = AnimationUtils.loadAnimation(this, R.anim.frombottom2);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.fromright);
        fromLeft = AnimationUtils.loadAnimation(this, R.anim.fromleft);

        art.startAnimation(fromBottom2);
        view.startAnimation(fromBottom3);
        completeNameField.startAnimation(fromLeft);
        completeNameIcon.startAnimation(fromLeft);
        cardNameField.startAnimation(fromLeft);
        usernameIcon2.startAnimation(fromLeft);
        cardNumberField.startAnimation(fromLeft);
        cardNumberIcon.startAnimation(fromLeft);
        expirationDateField.startAnimation(fromLeft);
        expirationDateIcon.startAnimation(fromLeft);
        title.startAnimation(fromBottom3);
        subtitle.startAnimation(fromBottom3);
        uploadedPicture.startAnimation(fromBottom3);
        viewCardButton.startAnimation(fromLeft);
        saveProfileButton.startAnimation(fromRight);
        logoutIcon.startAnimation(fromLeft);
        logoutText.startAnimation(fromLeft);

        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(BankActivity.this, "Signing out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        viewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(BankActivity.this, "Wait for the data to sync!", Toast.LENGTH_SHORT).show();
               // try {
                    //Thread.sleep(5000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                openUserInformation();
            }
        });

        completeNameField.setText(getUserCompleteName);


        //Save profile data
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(userCompleteName, completeNameField.getText().toString());
                editor.apply();

                SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString(cardName, cardNameField.getText().toString());
                editor2.apply();

                SharedPreferences sharedPreferences3 = getSharedPreferences(SHARED_PREFS3, MODE_PRIVATE);
                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                editor3.putString(cardNumber, cardNumberField.getText().toString());
                editor3.apply();

                SharedPreferences sharedPreferences4 = getSharedPreferences(SHARED_PREFS4, MODE_PRIVATE);
                SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                editor4.putString(expirationDate, expirationDateField.getText().toString());
                editor4.apply();

                Toast.makeText(BankActivity.this, "Wait for the data to sync!", Toast.LENGTH_SHORT).show();
                openUserInformation();
            }
        });

        //Storage Permision
        uploadedPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BankActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        uploadedPicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select an image!"), GALLERY_REQUEST_CODE);
                        }
                    });
                } else {
                    requestStoragePermission();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageData = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                uploadedPicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(imageData != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(userID + "~");
            ref.putFile(imageData)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(BankActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(BankActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openUserInformation() {
        Intent intent = new Intent(this, UserInformationActivity.class);
        startActivity(intent);
    }

    //Read external storage
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BankActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

}
