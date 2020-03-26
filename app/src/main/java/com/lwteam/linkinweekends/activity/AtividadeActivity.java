package com.lwteam.linkinweekends.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.model.Locais;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class AtividadeActivity extends AppCompatActivity {

    //CARREGAR LOCAIS
    private CarouselView caroucelViewLocal;

    Button btsobreAtividade;
    Button bthorariosAtividade;
    Button btcomentariosAtividade;
    Button btcustosAtividade;

    //DIALOG SOBRE
    Dialog SobreDialog;
    TextView tvTexSobre;
    ImageView btFecharDialogSobre;

    //DIALOG HORARIOS
    Dialog HorariosDialog;
    TextView tvHoraUtilAbre,tvHoraUtilFecha;
    TextView tvHoraSabadoAbre,tvHoraSabadoFecha;
    TextView tvHoraDomingoAbre,tvHoraDomingoFecha;
    ImageView btFecharDialogHora;

    //DIALOG CUSTOS
    Dialog CustosDialog;
    TextView tvTexCusto;
    ImageView getBtFecharDialogCustos;


    private Locais localSelecionado;

    // ACESSO A LOCALIZACAO
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        inicializarComponentes();


        //RECUPERAR DADOS DOS LOCAIS PARA EXIBICAO
        localSelecionado = (Locais) getIntent().getSerializableExtra("localSelecionado");

        if(localSelecionado !=null){
            getSupportActionBar().setTitle(localSelecionado.getNomeAtividade());
            btsobreAtividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sobreDialog();

                }
            });
            bthorariosAtividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horariosDialog();
                }
            });
            btcustosAtividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custosDialog();
                }
            });

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = localSelecionado.getFotosAtividade().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };
            caroucelViewLocal.setPageCount(localSelecionado.getFotosAtividade().size());
            caroucelViewLocal.setImageListener(imageListener);
        }
    }
    //SUB-MENUS
    public void sobreDialog(){
        SobreDialog.setContentView(R.layout.dialog_sobre_local);
        btFecharDialogSobre = (ImageView) SobreDialog.findViewById(R.id.btnFecharDialogSobre);
        tvTexSobre = (TextView) SobreDialog.findViewById(R.id.tvTextoSobre);
        tvTexSobre.setText(localSelecionado.getDescricaoAtividade());
        btFecharDialogSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SobreDialog.dismiss();
            }
        });
        SobreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SobreDialog.show();
    }
    public void horariosDialog(){
        HorariosDialog.setContentView(R.layout.dialog_horarios_local);
        btFecharDialogHora = (ImageView) HorariosDialog.findViewById(R.id.btnFecharDialogHorarios);

        tvHoraUtilAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraUtilAbre);
        tvHoraUtilAbre.setText(localSelecionado.getHoraAbreUtilAtividade());
        tvHoraUtilFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraUtilFecha);
        tvHoraUtilFecha.setText(localSelecionado.getHoraFechaUtilAtividade());
        tvHoraSabadoAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraSabadoAbre);
        tvHoraSabadoAbre.setText(localSelecionado.getHoraAbreSabadoAtividade());
        tvHoraSabadoFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraSabadoFecha);
        tvHoraSabadoFecha.setText(localSelecionado.getHoraFechaSabadoAtividade());
        tvHoraDomingoAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraDomingoAbre);
        tvHoraDomingoAbre.setText(localSelecionado.getHoraAbreDomingoAtividade());
        tvHoraDomingoFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraDomingoFecha);
        tvHoraDomingoFecha.setText(localSelecionado.getHoraFechaDomingoAtividade());

        btFecharDialogHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorariosDialog.dismiss();
            }
        });
        HorariosDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        HorariosDialog.show();
    }
    public void custosDialog(){
        CustosDialog.setContentView(R.layout.dialog_custos_local);
        getBtFecharDialogCustos = (ImageView) CustosDialog.findViewById(R.id.btnFecharDialogCustos);
        tvTexCusto = (TextView) CustosDialog.findViewById(R.id.tvTextoCusto);
        tvTexCusto.setText(localSelecionado.getCustosAtividade());
        getBtFecharDialogCustos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustosDialog.dismiss();
            }
        });
        CustosDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustosDialog.show();
    }
    //FINAL DOS SUB MENUS

    //INICIALIZA OS COMPONENTES DA TELA
    private void inicializarComponentes(){

        SobreDialog = new Dialog(this);
        HorariosDialog = new Dialog(this);
        CustosDialog = new Dialog(this);

        caroucelViewLocal = findViewById(R.id.carouselAtividade);
        btsobreAtividade =findViewById(R.id.btnSobreAtividade);
        bthorariosAtividade=findViewById(R.id.btnHorariosAtividade);
        btcustosAtividade=findViewById(R.id.btnCustoAtividade);
    }
    //CASO A PERMISSÃO DE LOCALIZACAO SEJA CANCELADA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if(permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertavalidacaopermissao();
            }
        }
    }
    // CASO A PERMISSÃO SEJA CANCELADA
    private void alertavalidacaopermissao(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
