package coming.example.lkc.bottomnavigationbar;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.dao.Users;

/**
 * Created by lkc on 2017/8/28.
 */
public class JBS_Activity extends AppCompatActivity {
    private List<Users> usersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jianbianse_layout);
        usersList = DataSupport.findAll(Users.class);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.user_list_rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserList_Adpater());
    }

    class UserList_Adpater extends RecyclerView.Adapter<UserList_Adpater.UserList_ViewHolder> {

        class UserList_ViewHolder extends RecyclerView.ViewHolder {
            TextView id, username, password, path;

            public UserList_ViewHolder(View view) {
                super(view);
                id = (TextView) view.findViewById(R.id.userlist_id);
                username = (TextView) view.findViewById(R.id.userlist_username);
                password = (TextView) view.findViewById(R.id.userlist_password);
                path = (TextView) view.findViewById(R.id.userlist_path);
            }
        }

        @Override
        public UserList_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_rc, parent, false);
            return new UserList_ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(UserList_ViewHolder holder, int position) {
            holder.id.setText(String.valueOf(usersList.get(position).getId()));
            holder.username.setText(usersList.get(position).getUsername());
            holder.password.setText(usersList.get(position).getPassword());
            holder.path.setText(usersList.get(position).getPath());

        }

        @Override
        public int getItemCount() {
            return usersList == null ? 0 : usersList.size();
        }
    }
}