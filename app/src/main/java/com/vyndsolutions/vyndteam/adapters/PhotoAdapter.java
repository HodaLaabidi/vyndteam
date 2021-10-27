package com.vyndsolutions.vyndteam.adapters;

        import android.content.Context;
        import android.net.Uri;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.ProgressBar;
        import com.vyndsolutions.vyndteam.R;
        import com.vyndsolutions.vyndteam.models.ImageGalerie;
        import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

        import java.io.File;
        import java.util.ArrayList;
/**
 * Created by Hoda on 22/04/2018.
 */

public class PhotoAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener{

    NetworkImageView ivPhoto;
    private ArrayList<ImageGalerie> imageGalerieArrayList;
    ProgressBar progressBar;
    Context context ;

    public PhotoAdapter(Context context, ArrayList<ImageGalerie> imageGaleries) {
        this.context = context;
        this.imageGalerieArrayList=imageGaleries;


    }

    @Override
    public int getCount() {
        return imageGalerieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageGalerieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageGalerieArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_photo, parent, false);
        ivPhoto = view.findViewById(R.id.iv_photo);
        progressBar = view.findViewById(R.id.progresssBar);

        if(imageGalerieArrayList.size()!=0)
        {
            progressBar.setVisibility(View.VISIBLE);
            ivPhoto.setImageUrlWithoutBorderWithoutResizing(imageGalerieArrayList.get(position).getImage(),R.drawable.empty_business_photo,progressBar);

        }

        return view;
    }

    public void refresh(Context context, ArrayList<ImageGalerie> imageGaleries)
    {
        this.context = context;
        this.imageGalerieArrayList=imageGaleries;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }



}
