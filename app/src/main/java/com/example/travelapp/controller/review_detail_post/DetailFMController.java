package com.example.travelapp.controller.review_detail_post;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.controller.homefragment.InterfaceHomeFMController;
import com.example.travelapp.function_util.GetAllPostFromFirebaseStorage;
import com.example.travelapp.function_util.GetUserFromFireStorage;
import com.example.travelapp.model.Post;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetCurrentUserListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostByIDListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailFMController implements InterfaceDetailFMController {
    private GetAllPostFromFirebaseStorage getAllPostFromFirebaseStorage;
    private Context context;

    public DetailFMController(Context context) {
        this.context = context;
    }

    @Override
    public void setDataForDetailPostFM(List<ImageView> listImageView, List<TextView> listTextView,String postID) {
        getAllPostFromFirebaseStorage = new GetAllPostFromFirebaseStorage();
        getAllPostFromFirebaseStorage.getPostFromFirebaseByID(postID, new InterfaceEventGetPostByIDListener() {

            @Override
            public void getPostByIDSuccess(Post post) {
                setImageByGlide(post,listImageView);
                setTextForTextView(post,listTextView);

            }

            @Override
            public void getPostsFail(String isFail) {

            }
        });
    }

    private void setTextForTextView(Post post, List<TextView> listTextView) {
        listTextView.get(0).setText(post.getTouristName());
      //  listTextView.get(1).setText();
    }

    private void setImageByGlide(Post post, List<ImageView> listImageView) {
        Glide.with(context)
                .load(post.getImage0())
                .into(listImageView.get(0));
        Glide.with(context)
                .load(post.getImage1())
                .into(listImageView.get(1));
        Glide.with(context)
                .load(post.getImage2())
                .into(listImageView.get(2));
        Glide.with(context)
                .load(post.getImage3())
                .into(listImageView.get(3));
        Glide.with(context)
                .load(post.getImag4())
                .into(listImageView.get(4));
    }
}
