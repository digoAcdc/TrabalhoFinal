package com.fiap.rodrigo.trabalhofinal.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;
import com.fiap.rodrigo.trabalhofinal.R;

import java.io.File;
import java.util.List;

/**
 * Created by rodrigo on 07/02/2016.
 */
public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private Context context;
    private List<Restaurante> lstRestaurante;
    private final RestauranteOnClickListener onClickListener;

    public interface RestauranteOnClickListener {
        public void OnClickRestaurante(View view, int index);
    }

    public RestauranteAdapter(Context context, List<Restaurante> lstRestaurante, RestauranteOnClickListener onClickListener) {
        this.context = context;
        this.lstRestaurante = lstRestaurante;
        this.onClickListener = onClickListener;
    }

    @Override
    public RestauranteAdapter.RestauranteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_restaurante, parent, false);
        RestauranteViewHolder holder = new RestauranteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RestauranteAdapter.RestauranteViewHolder holder, final int position) {
        Restaurante r = lstRestaurante.get(position);

        // holder.tvLatitude.setText(r.getLatitude());
        // holder.tvLongitude.setText(r.getLongitude());
        //holder.ivRestaurante.
        holder.tiNomeRestaurante.setText(r.getNome());
        holder.tiCustoMedio.setText(r.getCustoMedio());
        holder.tiTelefone.setText(r.getTelefone());
        holder.tiObservacao.setText(r.getObservacao());
        holder.spTipo.setText(r.getTipo());
        if (r.getCaminhoImagem() != null) {
            File file = new File(r.getCaminhoImagem());
            if (file.exists()) {
                holder.ivRestaurante.setImageURI(Uri.parse(r.getCaminhoImagem()));
            }
        }
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.OnClickRestaurante(holder.view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstRestaurante.size();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        TextView tvLatitude;
        TextView tvLongitude;
        ImageView ivRestaurante;
        TextView tiNomeRestaurante;
        TextView tiCustoMedio;
        TextView tiTelefone;
        TextView tiObservacao;
        TextView spTipo;
        ImageView ivOpcoes;
        ProgressBar pbRestaurante;
        private View view;

        public RestauranteViewHolder(View view) {
            super(view);
            this.view = view;
            // this.tvLatitude = (TextView) view.findViewById(R.id.tvValorLatitude);
            //  this.tvLongitude = (TextView) view.findViewById(R.id.tvValorLongitude);
            // this.ivRestaurante = (ImageView) view.findViewById(R.id.ivRestaurante);
            this.tiNomeRestaurante = (TextView) view.findViewById(R.id.tvNomeRestaurante);
            this.tiCustoMedio = (TextView) view.findViewById(R.id.tvCustoMedio);
            this.tiTelefone = (TextView) view.findViewById(R.id.tvTelefone);
            this.tiObservacao = (TextView) view.findViewById(R.id.tvObservacao);
            this.ivRestaurante = (ImageView) view.findViewById(R.id.ivRestaurante);
            this.spTipo = (TextView) view.findViewById(R.id.tvTipo);

            this.ivOpcoes = (ImageView) view.findViewById(R.id.ivOpcoes);
            this.pbRestaurante = (ProgressBar) view.findViewById(R.id.pbRestaurante);
        }
    }
}

