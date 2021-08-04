package com.bebo.manga;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyVerticalViewAdapter;
import com.bebo.manga.adapter.MyViewPageAdapter;
import com.bebo.manga.model.Chapter;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

public class ViewComicActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView txt_chapter_name;
    View back,next;
    RecyclerView viewRecycler;
    TextView pageText;

    private static boolean bookEffect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        //viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewRecycler = (RecyclerView)findViewById(R.id.recycler_image);
        viewRecycler.setHasFixedSize(true);
        viewRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        txt_chapter_name = (TextView)findViewById(R.id.txt_chapter_name);
        back = (View)findViewById(R.id.chapter_back);
        next = (View)findViewById(R.id.chapter_next);
        pageText = (TextView)findViewById(R.id.page_txt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.chapterIndex == 0)
                {
                    Toast.makeText(ViewComicActivity.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                }else {
                    Common.chapterIndex--;
                    Common.chapterSelected = Common.chapterList.get(Common.chapterIndex);
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.chapterIndex == Common.chapterList.size()-1)
                {
                    Toast.makeText(ViewComicActivity.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                }else {
                    Common.chapterIndex++;
                    Common.chapterSelected = Common.chapterList.get(Common.chapterIndex);
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });
        fetchLinks(Common.chapterSelected);
    }

    private void fetchLinks(Chapter chapter) {
        if(chapter.Links != null)
        {
            if(chapter.Links.size() > 0)
            {
                MyViewPageAdapter adapter = new MyViewPageAdapter(getBaseContext(), chapter.Links);
                MyVerticalViewAdapter verticalViewAdapter = new MyVerticalViewAdapter(getBaseContext(), chapter.Links, Common.online);
                Log.d("SIZE", String.valueOf(new MyViewPageAdapter(getBaseContext(), chapter.Links).getCount()));
                if(bookEffect)
                {
                    viewPager.setAdapter(adapter);
                    //animation
                    BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                    bookFlipPageTransformer.setScaleAmountPercent(10f);
                    viewPager.setPageTransformer(true, bookFlipPageTransformer);
                }else{
                    viewRecycler.setAdapter(verticalViewAdapter);
                }
                txt_chapter_name.setText(Common.formatString(Common.chapterSelected.Name));

            }else {
                Toast.makeText(this, "no image here", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "this chapter is translating", Toast.LENGTH_SHORT).show();
        }

    }
}