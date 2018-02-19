import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by swaini negi on 19/02/2018.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyRecyclerHolder> {
    private LayoutInflater inflater;
    private List<InformationObject> list;

    RecyclerAdapter(Context context, List<InformationObject> list) {
        inflater=LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public MyRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecyclerHolder(inflater.inflate(R.layout.custom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRecyclerHolder holder, int position) {
        holder.time.setText(list.get(position).getTime());
        holder.post.setText(list.get(position).getPost());
    }

    @Override
    public int getItemCount() {return list.size();
    }

    public class MyRecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView imageview, imageview2;
        private TextView post, time;

        public MyRecyclerHolder(View itemView) {
            super(itemView);
            post = (TextView) itemView.findViewById(R.id.textview);
            time = (TextView) itemView.findViewById(R.id.textview2);
            imageview = (ImageView) itemView.findViewById(R.id.imageView);
            imageview2 = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }}