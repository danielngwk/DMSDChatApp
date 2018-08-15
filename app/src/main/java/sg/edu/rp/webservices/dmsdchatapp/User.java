package sg.edu.rp.webservices.dmsdchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private DatabaseReference nameListRef, existingName;
    private FirebaseDatabase firebaseDatabase;

    EditText etDisplayName;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        etDisplayName = (EditText)findViewById(R.id.editTextDisplayName);
        btnSubmit = (Button)findViewById(R.id.buttonSubmit);

        fbAuth = FirebaseAuth.getInstance();

        fbUser = fbAuth.getCurrentUser();
        String uid = fbUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        nameListRef = firebaseDatabase.getReference("profiles/" + uid);

        existingName = nameListRef.child(uid);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = fbUser.getUid();
                String displayName = etDisplayName.getText().toString();
                nameListRef.setValue(displayName);
                Intent i = new Intent(User.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            logout();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }else{



            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        fbAuth.signOut();
    }
}