package com.lwteam.linkinweekends.activity;

import android.Manifest;
import android.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.Permissoes;
import com.lwteam.linkinweekends.model.Locais;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

public class LocalActivity extends AppCompatActivity {

    private CarouselView caroucelViewLocal;

    Button btsobre;
    Button btatividades;
    Button bthorarios;
    Button btcomentarios;
    Button btcustos;
    Button btmaisinfo;

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

    //DIALOG MAIS INFORMACOES
    Dialog MaisinfoDialog;
    ImageView getBtFecharDialogMaisinfo;
    TextView tvTexEmailLocal;
    TextView tvTexTelefoneLocal;
    TextView tvTexRua,tvTexNumero,tvTexBairro,tvTexCep,tvtexCidade,tvtexEstado;



    private Locais localSelecionado;
    private ImageView btMaps;
    public String estado,regiao,cidade,id;
    public TextView campoEstadoLocal,campoRegiaoLocal,campoCidadeLocal,campoIdLocal;

    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local);

        inicializarComponentes();


        //RECUPERAR DADOS DOS LOCAIS PARA EXIBICAO
        localSelecionado = (Locais) getIntent().getSerializableExtra("localSelecionado");

        if(localSelecionado !=null){

            getSupportActionBar().setTitle(localSelecionado.getNome());
            btsobre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sobreDialog();

                }
            });
            bthorarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horariosDialog();
                }
            });
            btcomentarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LocalActivity.this,ComentariosActivity.class);
                    i.putExtra("estado",localSelecionado.getEstado());
                    i.putExtra("regiao",localSelecionado.getRegiao());
                    i.putExtra("cidade",localSelecionado.getCidade());
                    i.putExtra("id",localSelecionado.getIdLocal());
                    startActivity(i);
                }
            });
            btcustos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custosDialog();
                }
            });
            btmaisinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    maisinfoDialog();
                }
            });

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = localSelecionado.getFotos().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };
            btatividades.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LocalActivity.this,ListaAtividadesActivity.class);
                    i.putExtra("estado",localSelecionado.getEstado());
                    i.putExtra("regiao",localSelecionado.getRegiao());
                    i.putExtra("cidade",localSelecionado.getCidade());
                    i.putExtra("id",localSelecionado.getIdLocal());
                    startActivity(i);
                }
            });
            caroucelViewLocal.setPageCount(localSelecionado.getFotos().size());
            caroucelViewLocal.setImageListener(imageListener);
            BotaoMaps();
        }
    }
    //RECUPERA OS TEXTOS "GETTEXT"
    public void sobreDialog(){
        SobreDialog.setContentView(R.layout.dialog_sobre_local);
        btFecharDialogSobre = (ImageView) SobreDialog.findViewById(R.id.btnFecharDialogSobre);
        tvTexSobre = (TextView) SobreDialog.findViewById(R.id.tvTextoSobre);
        tvTexSobre.setText(localSelecionado.getDescricao());
        btFecharDialogSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SobreDialog.dismiss();
            }
        });
        SobreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SobreDialog.show();
    }
    //RECUPERA OS TEXTOS "GETTEXT"
    public void horariosDialog(){
        HorariosDialog.setContentView(R.layout.dialog_horarios_local);
        btFecharDialogHora = (ImageView) HorariosDialog.findViewById(R.id.btnFecharDialogHorarios);

        tvHoraUtilAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraUtilAbre);
        tvHoraUtilAbre.setText(localSelecionado.getHoraAbreUtil());
        tvHoraUtilFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraUtilFecha);
        tvHoraUtilFecha.setText(localSelecionado.getHoraFechaUtil());
        tvHoraSabadoAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraSabadoAbre);
        tvHoraSabadoAbre.setText(localSelecionado.getHoraAbreSabado());
        tvHoraSabadoFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraSabadoFecha);
        tvHoraSabadoFecha.setText(localSelecionado.getHoraFechaSabado());
        tvHoraDomingoAbre = (TextView) HorariosDialog.findViewById(R.id.tvHoraDomingoAbre);
        tvHoraDomingoAbre.setText(localSelecionado.getHoraAbreDomingo());
        tvHoraDomingoFecha = (TextView) HorariosDialog.findViewById(R.id.tvHoraDomingoFecha);
        tvHoraDomingoFecha.setText(localSelecionado.getHoraFechaDomingo());

        btFecharDialogHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorariosDialog.dismiss();
            }
        });
        HorariosDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        HorariosDialog.show();
    }
    //RECUPERA OS TEXTOS "GETTEXT"
    public void custosDialog(){
        CustosDialog.setContentView(R.layout.dialog_custos_local);
        getBtFecharDialogCustos = (ImageView) CustosDialog.findViewById(R.id.btnFecharDialogCustos);
        tvTexCusto = (TextView) CustosDialog.findViewById(R.id.tvTextoCusto);
        tvTexCusto.setText(localSelecionado.getCustos());
        getBtFecharDialogCustos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustosDialog.dismiss();
            }
        });
        CustosDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustosDialog.show();
    }
    //RECUPERA OS TEXTOS "GETTEXT"
    public void maisinfoDialog(){
        MaisinfoDialog.setContentView(R.layout.dialog_maisinformacoes_local);
        getBtFecharDialogMaisinfo = (ImageView) MaisinfoDialog.findViewById(R.id.btnFecharDialogMaisInfo);

        tvTexEmailLocal = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoEmail);
        tvTexEmailLocal.setText(localSelecionado.getEmailLocal());

        tvTexTelefoneLocal = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoTelefone);
        tvTexTelefoneLocal.setText(localSelecionado.getTelefoneLocal());

        tvTexRua = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoRua);
        tvTexRua.setText(localSelecionado.getRua());

        tvTexNumero = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoNumero);
        tvTexNumero.setText(localSelecionado.getNumero());

        tvTexBairro = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoBairro);
        tvTexBairro.setText(localSelecionado.getBairro());

        tvTexCep = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoCep);
        tvTexCep.setText(localSelecionado.getCEP());

        tvtexCidade = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoCidade);
        tvtexCidade.setText(localSelecionado.getCidade());

        tvtexEstado = (TextView) MaisinfoDialog.findViewById(R.id.tvTextoEstado);
        tvtexEstado.setText(localSelecionado.getEstado());

        getBtFecharDialogMaisinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaisinfoDialog.dismiss();
            }
        });
        MaisinfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MaisinfoDialog.show();
    }

    private void inicializarComponentes(){

        SobreDialog = new Dialog(this);
        HorariosDialog = new Dialog(this);
        CustosDialog = new Dialog(this);
        MaisinfoDialog = new Dialog(this);

        caroucelViewLocal = findViewById(R.id.carouselLocal);
        btsobre =findViewById(R.id.btnSobre);
        btatividades=findViewById(R.id.btnAtividades);
        bthorarios=findViewById(R.id.btnHorarios);
        btcomentarios=findViewById(R.id.btnComentarios);
        btcustos=findViewById(R.id.btnCusto);
        btmaisinfo=findViewById(R.id.btnMaisInformacoes);
        btMaps=findViewById(R.id.btnMapsLocal);
    }

    //ATRIBUI AS FUNCOES NO BOTAO DO MAPS
    public void BotaoMaps(){

        btMaps.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onClick(View v) {
                Permissoes.validarPermissoes(permissoes, LocalActivity.this, 1);
                String lat="";
                String lon="";

                lat=String.valueOf(localSelecionado.getLatitude());
                lon=String.valueOf(localSelecionado.getLongitude());

                //ABRIR ROTA
                String LatLong = lat + "," + lon;
                Uri uri = Uri.parse("google.navigation:q="+LatLong+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");

                //VERIFICA SE O GOOGLE MAPS ESTA INSTALADO NO DISPOSITIVO
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activies = packageManager.queryIntentActivities(mapIntent,0);
                boolean isIntentSafe = activies.size()>0;

                //INICIA O MAPS SE O GOOGLE MAPS ESTA DISPONIVEL
                if(isIntentSafe) {
                    startActivity(mapIntent);
                }else {
                    Toast.makeText(LocalActivity.this,
                            "Google Maps Não esta Instalado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //PERMISSAO PRA ACESSAR A LOCALIZACAO E ABRIR O MAPS ESTA BUGADO TEM QUE ARRUMAR -GERALDO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if(permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertavalidacaopermissao();
            }
        }
    }
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
