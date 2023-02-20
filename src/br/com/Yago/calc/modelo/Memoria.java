package br.com.Yago.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

	private enum TipoComando {
		ZERAR, NUMERO, DIVISAO, SOMA, MULTIPLICACAO, SUB, VIRGULA, IGUAL;
	};

	private static Memoria instancia = new Memoria();

	private final List<MemoriaObservador> observadores = new ArrayList<>();

	private TipoComando ultimaOperacao = null;
	private boolean substtuir = false;
	private String textoAtual = "";
	private String textoBuffer = "";

	private Memoria() {

	}

	public static Memoria getInstancia() {
		return instancia;
	}

	public void adicionarObservador(MemoriaObservador observador) {
		observadores.add(observador);
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	public void processarComando(String texto) {
		TipoComando tipoComando = detectarTipoComando(texto);
		if (tipoComando == null) {
			return;
		} else if (tipoComando == tipoComando.ZERAR) {
			textoAtual = "";
			textoBuffer = "";
			substtuir = false;
			ultimaOperacao = null;
		} else if (tipoComando == tipoComando.NUMERO || tipoComando == tipoComando.VIRGULA) {
			textoAtual = substtuir ? texto : textoAtual + texto;
			substtuir = false;
		} else {
			substtuir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = textoAtual;
			ultimaOperacao = tipoComando;

		}

		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() {
		if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(textoBuffer.replace(",", "."));

		double resultado = 0;

		if (ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if (ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		} else if (ultimaOperacao == TipoComando.DIVISAO) {
			resultado = numeroBuffer / numeroAtual;
		} else if (ultimaOperacao == TipoComando.MULTIPLICACAO) {
			resultado = numeroBuffer * numeroAtual;
		}

		String resultadoString = Double.toString(resultado).replace(".", ",");
		boolean inteiro = resultadoString.endsWith(",0");

		return inteiro ? resultadoString.replace(",0", "") : resultadoString;
	}

	private TipoComando detectarTipoComando(String texto) {
		if (textoAtual.isEmpty() && texto == "0") {
			return null;
		}
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;

		} catch (NumberFormatException e) {
			// quando não for numero...

			if ("AC".equals(texto)) {
				return TipoComando.ZERAR;
			} else if ("/".equals(texto)) {
				return TipoComando.DIVISAO;
			} else if ("*".equals(texto)) {
				return TipoComando.MULTIPLICACAO;
			} else if ("+".equals(texto)) {
				return TipoComando.SOMA;
			} else if ("-".equals(texto)) {
				return TipoComando.SUB;
			} else if (",".equals(texto) && !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;
			} else if ("=".equals(texto)) {
				return TipoComando.IGUAL;
			}

		}
		return null;
	}
}
