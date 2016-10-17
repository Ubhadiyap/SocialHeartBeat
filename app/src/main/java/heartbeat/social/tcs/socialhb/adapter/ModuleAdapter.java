package heartbeat.social.tcs.socialhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.ModuleItem;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

/**
 * Created by admin on 22/07/16.
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewModuleAdapter> {

    private List<ModuleItem> modules;
    private Context context;


    public ModuleAdapter(List<ModuleItem> c_modules, Context c_ctx) {
        this.modules = c_modules;
        this.context = c_ctx;
    }

    @Override
    public ViewModuleAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_single_item, parent, false);
        ViewModuleAdapter avh = new ViewModuleAdapter(v, context, modules);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewModuleAdapter holder, int position) {
        holder.v_txtView.setText(modules.get(position).getModuleName());


        Log.e("Module Image Path : ", modules.get(position).getModuleIcon());

        ModuleSelector moduleSelector = new ModuleSelector();
        Integer id = modules.get(position).getId();
        String imageName = moduleSelector.getModuleNameByModuleId(id);
        String uri = "@drawable/" + imageName.toLowerCase();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Picasso.with(context).load(imageResource).into(holder.v_imageView);


    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewModuleAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {


        //  public TextView v_txtView;
        public ImageView v_imageView;
        public TextView v_txtView;
        public CardView v_cardView;
        public Context v_ctx;
        public List<ModuleItem> v_modules = new ArrayList<ModuleItem>();

        public ViewModuleAdapter(View itemView, Context c_ctx, List<ModuleItem> c_modules) {
            super(itemView);
            v_txtView = (TextView) itemView.findViewById(R.id.moduleTxtView);
            v_imageView = (ImageView) itemView.findViewById(R.id.moduleImageView);
            v_cardView = (CardView) itemView.findViewById(R.id.moduleCard);

            //Getting Screen Size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            //setting cardsize
            v_cardView.getLayoutParams().height = (deviceHeight * 43) / 100;

            v_imageView.setY((deviceHeight * 10) / 100);
            v_imageView.setX((deviceHeight * 3) / 100);

            v_imageView.getLayoutParams().height = (deviceHeight * 120) / 100;
            v_imageView.getLayoutParams().width = (deviceHeight * 20) / 100;

            v_txtView.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            this.v_ctx = c_ctx;
            this.v_modules = c_modules;

            itemView.setOnClickListener(this);
            //v_cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            int module_id = v_modules.get(position).getId();
            String module_name = v_modules.get(position).getModuleName();


            ModuleSelector moduleSelector = new ModuleSelector();
            String pack_name = "heartbeat.social.tcs.socialhb.activity.modules.";
            String main_module_name = moduleSelector.getClassNameByModuleId(module_id);

            String cmplt_module_name = pack_name.concat(main_module_name);


            Intent intent = null;
            try {
                intent = new Intent(this.v_ctx, Class.forName(cmplt_module_name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.v_ctx.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}


