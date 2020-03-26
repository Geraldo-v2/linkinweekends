package com.lwteam.linkinweekends.activity;

import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.fragment.DestaquesFragment;
import com.lwteam.linkinweekends.fragment.FavoritosFragment;
import com.lwteam.linkinweekends.fragment.HomeFragment;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BARRA DE NOTIFICACOES TRANPARENTE
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FIM DO CODIGO DA BARRA TRANSPARENTE
        setContentView(R.layout.activity_menu);


        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();


        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //HABILITAR NAVEGACAO DO MENU INFERIOR
        habilitarNavegacao(bottomNavigationViewEx);

        //CONFIGURA QUAL FRAGMENT VAI SER INICALIZADO, INDICE COMECA EM 0
        final Menu menu = bottomNavigationViewEx.getMenu();
        final MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    //CONFIGURA AS FUNCOES DOS BOTOES DO NAVIGATION
    //HOME - FAVORITOS - ETC

    private void habilitarNavegacao(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();
                        return true;
                    case R.id.ic_fav:
                        fragmentTransaction.replace(R.id.viewPager, new FavoritosFragment()).commit();
                        return true;
                    case R.id.ic_destaques:
                        fragmentTransaction.replace(R.id.viewPager, new DestaquesFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
