package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OpeningActivity extends AppCompatActivity {

    private EditText playerNameEditText;
    private Button startGameButton, buttonChangePhoto, buttonInfo;
    private ImageView imageViewProfile;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        playerNameEditText = findViewById(R.id.playerNameEditText);
        startGameButton = findViewById(R.id.startGameButton);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);
        buttonInfo = findViewById(R.id.buttonInfo);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String savedName = prefs.getString("PLAYER_NAME", "");
        playerNameEditText.setText(savedName);

        loadProfileImage();

        if (getIntent().getBooleanExtra("FROM_HISTORY", false)) {
            showNameChangeDialog(savedName);
        }

        startGameButton.setOnClickListener(v -> {
            String playerName = playerNameEditText.getText().toString().trim();
            if (!playerName.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("PLAYER_NAME", playerName);
                editor.apply();

                Intent intent = new Intent(OpeningActivity.this, MainActivity.class);
                intent.putExtra("PLAYER_NAME", playerName);
                startActivity(intent);
            } else {
                playerNameEditText.setError("יש להזין שם");
            }
        });

        buttonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        buttonChangePhoto.setOnClickListener(v -> showImagePickerDialog());

        // הרשמות לאקטיביטי תוצאה (Camera)
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        saveImageToInternalStorage(imageBitmap);
                        imageViewProfile.setImageBitmap(imageBitmap);
                    }
                }
        );

        // הרשמות לאקטיביטי תוצאה (Gallery)
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            saveImageToInternalStorage(bitmap);
                            imageViewProfile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void showNameChangeDialog(String currentName) {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        new AlertDialog.Builder(this)
                .setTitle("האם לשנות שם שחקן?")
                .setMessage("השם הנוכחי הוא: " + currentName + "\nהאם ברצונך לשנות אותו?")
                .setPositiveButton("כן", (dialog, which) -> {
                    // מחיקה מה-SharedPreferences
                    prefs.edit().remove("PLAYER_NAME").apply();
                    // איפוס שדה הטקסט
                    playerNameEditText.setText("");

                    // מחיקה של תמונת הפרופיל ושחזור לתמונה ברירת מחדל
                    File file = new File(getFilesDir(), "profile.jpg");
                    if (file.exists()) file.delete();

                    // הצגת אווטאר ברירת מחדל (למשל imageViewProfile_default)
                    imageViewProfile.setImageResource(R.drawable.profile_avatar);
                })
                .setNegativeButton("לא", null)
                .show();
    }




    private void showImagePickerDialog() {
        String[] options = {"צלם תמונה", "בחר מהגלריה"};

        new AlertDialog.Builder(this)
                .setTitle("בחר אפשרות")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(takePictureIntent);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryLauncher.launch(pickIntent);
                    }
                })
                .show();
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        try {
            File file = new File(getFilesDir(), "profile.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfileImage() {
        File file = new File(getFilesDir(), "profile.jpg");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageViewProfile.setImageBitmap(bitmap);
        } else {
            imageViewProfile.setImageResource(R.drawable.profile_avatar);
        }
    }

}
