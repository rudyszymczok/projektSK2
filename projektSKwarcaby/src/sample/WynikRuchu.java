package sample;

public class WynikRuchu {  //typ ruchu wraz ze zbijanym pionkem
    private TypRuchu typ_ruchu;

    public TypRuchu getTyp(){
        return typ_ruchu;
    }

    private Pionek pionek;

    public Pionek getPionek(){
        return pionek;
    }

    public WynikRuchu(TypRuchu typ_ruchu){
        this(typ_ruchu, null);
    }

    public WynikRuchu(TypRuchu typ_ruchu, Pionek pionek){
        this.typ_ruchu = typ_ruchu;
        this.pionek = pionek;
    }
}
