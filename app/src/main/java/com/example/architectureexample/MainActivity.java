package com.example.architectureexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.adapter.UserAdapter;
import com.example.architectureexample.databinding.ActivityMainBinding;
import com.example.architectureexample.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainxml;

    public static final int ADD_USER_REQUEST = 1;
    public static final int EDIT_USER_REQUEST = 2;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainxml = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainxml.getRoot());

       // FloatingActionButton buttonAddUser = findViewById(R.id.button_add_user);
        mainxml.buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditUserActivity.class);
                startActivityForResult(intent, ADD_USER_REQUEST);
            }
        });
    //    RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mainxml.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainxml.recyclerView.setHasFixedSize(true);
        final UserAdapter adapter = new UserAdapter();
        mainxml.recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                adapter.submitList(users);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                userViewModel.delete(adapter.getUserAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mainxml.recyclerView);
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(MainActivity.this, AddEditUserActivity.class);
                intent.putExtra(AddEditUserActivity.EXTRA_ID, user.getId());
                intent.putExtra(AddEditUserActivity.EXTRA_TITLE, user.getName());
                intent.putExtra(AddEditUserActivity.EXTRA_EMAIL, user.getEmail());
                intent.putExtra(AddEditUserActivity.EXTRA_PHONE, user.getPhone());
                intent.putExtra(AddEditUserActivity.EXTRA_CODE, user.getCode());
                intent.putExtra(AddEditUserActivity.EXTRA_COUNTRY, user.getCountry());
                intent.putExtra(AddEditUserActivity.EXTRA_DESCRIPTION, user.getDesc());
                intent.putExtra(AddEditUserActivity.EXTRA_GENDER, user.getGender());
                intent.putExtra(AddEditUserActivity.EXTRA_PHOTO, user.getPhoto());

                startActivityForResult(intent, EDIT_USER_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditUserActivity.EXTRA_TITLE);
            String email = data.getStringExtra(AddEditUserActivity.EXTRA_EMAIL);
            String gender = data.getStringExtra(AddEditUserActivity.EXTRA_GENDER);
            String phone = String.valueOf(data.getStringExtra(AddEditUserActivity.EXTRA_PHONE));
            String country = data.getStringExtra(AddEditUserActivity.EXTRA_COUNTRY);
            String code = data.getStringExtra(AddEditUserActivity.EXTRA_CODE);
            String photo = data.getStringExtra(AddEditUserActivity.EXTRA_PHOTO);
            String description = data.getStringExtra(AddEditUserActivity.EXTRA_DESCRIPTION);




            User user = new User( title,email,gender,phone,country,code,description);
            user.setPhoto(photo);
            userViewModel.insert(user);
            Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_USER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditUserActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "User can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditUserActivity.EXTRA_TITLE);
            String email = data.getStringExtra(AddEditUserActivity.EXTRA_EMAIL);
            String gender = data.getStringExtra(AddEditUserActivity.EXTRA_GENDER);
            String phone = data.getStringExtra(AddEditUserActivity.EXTRA_PHONE);
            String photo = data.getStringExtra(AddEditUserActivity.EXTRA_PHOTO);
            String code = data.getStringExtra(AddEditUserActivity.EXTRA_CODE);
            String country = data.getStringExtra(AddEditUserActivity.EXTRA_COUNTRY);
            String description = data.getStringExtra(AddEditUserActivity.EXTRA_DESCRIPTION);

            User user = new User(title,email,gender,phone,country,code,description);
            user.setId(id);
            user.setPhoto(photo);
            userViewModel.update(user);
            Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User not saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_users:
                userViewModel.deleteAllUsers();
                Toast.makeText(this, "All users deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}