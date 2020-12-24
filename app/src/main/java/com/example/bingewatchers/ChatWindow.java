package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ChatWindow extends AppCompatActivity {
TextView grpName ;
Button show ;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_windiow);
        grpName=findViewById(R.id.grpName) ;
        bottom_sheet = findViewById(R.id.bottom_sheet);
        show=findViewById(R.id.show) ;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        if(getIntent().getSerializableExtra("Group Name")!=null)
            grpName.setText("In the window of \n"+getIntent().getSerializableExtra("Group Name").toString());
        else
            grpName.setText("Empty group namne");

    show.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                show.setText("Close sheet");
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                show.setText("Expand sheet");
            }
        }
    });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        show.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        show.setText("Expand Sheet");
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }
}