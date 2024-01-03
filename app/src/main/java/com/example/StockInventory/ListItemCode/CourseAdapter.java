package com.example.StockInventory.ListItemCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.StockInventory.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Viewholder> {

    private Context context;
    private ArrayList<CourseModel> courseModelArrayList;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcard_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.Viewholder holder, int position) {
        CourseModel model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        holder.courseRatingTV.setText("UOM: " + model.getCourse_rating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When item is clicked
                String clickedItemCode = model.getCourse_name();  // Assuming there's a getter method in your model
                // Pass this data to your desired function or activity
                notifyItemClicked(clickedItemCode);
            }
        });
    }

    // Notify activity when an item is clicked
    private void notifyItemClicked(String itemCode) {
        // Here, we'll use an interface callback or LocalBroadcast to communicate back to the Activity.
        // For simplicity, I'm just calling a static method in this example, but in a real-world scenario,
        // consider using interfaces, LiveData, or Broadcasts for cleaner architecture.

        itemsetting.onCourseItemClicked(context,itemCode);
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView courseNameTV, courseRatingTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
        }
    }
}

