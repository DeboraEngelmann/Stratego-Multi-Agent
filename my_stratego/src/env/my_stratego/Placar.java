// CArtAgO artifact code for project my_stratego

package my_stratego;

import cartago.*;

public class Placar extends Artifact {

	void init(int qtdPecasInicial) {
		defineObsProperty("pecasTimeAzul", qtdPecasInicial);
		defineObsProperty("pecasTimeVermelho", qtdPecasInicial);
	}

	@OPERATION
	void atualizaTime(String time) {
		if(time.equals("timeAzul")) {
			ObsProperty prop = getObsProperty("pecasTimeAzul");
			prop.updateValue(prop.intValue()-1);			
		}else {
			ObsProperty prop = getObsProperty("pecasTimeVermelho");
			prop.updateValue(prop.intValue()-1);
		}
	}
	
	@OPERATION
	void atualizaAmbos() {
		ObsProperty propA = getObsProperty("pecasTimeAzul");
		ObsProperty propV = getObsProperty("pecasTimeVermelho");
		propA.updateValue(propA.intValue()-1);	
		propV.updateValue(propV.intValue()-1);
	}
}

