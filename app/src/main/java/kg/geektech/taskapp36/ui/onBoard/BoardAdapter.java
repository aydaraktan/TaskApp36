package kg.geektech.taskapp36.ui.onBoard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{
    private OnItemClickListener onItemClickListener;
    private String [] titles = new String []{"Салам","привет","hello"};
    private String [] titles2 = new String []{"Кош келиниз","Добро пожаловать","Welcome"};
    private int [] pic = new int[]{R.drawable.hello1,R.drawable.hello2,R.drawable.hello3};
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_board,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textTitle2;
        private ImageView pica;
        private Button start;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textTitle2=itemView.findViewById(R.id.textDesc);
            pica=itemView.findViewById(R.id.imageView);
            start=itemView.findViewById(R.id.start);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        onItemClickListener.onClick(getAdapterPosition());
                }
            });

        }

        public void bind(int position) {
            textTitle.setText(titles[position]);
            textTitle2.setText(titles2[position]);
            pica.setImageResource(pic[position]);
            if(position==2)
            {
                start.setVisibility(View.VISIBLE);
            }
            else
            {
                start.setVisibility(View.GONE);
            }

        }
    }
}
