package br.com.alura.forum.config.validation;

public class ErroDeFormularioDTO {

		private String campo;
		private String erro;
		
		public ErroDeFormularioDTO(String field, String mensagem) {
			this.campo = field;
			this.erro = mensagem;
		}
		public String getCampo() {
			return campo;
		}
		public void setCampo(String campo) {
			this.campo = campo;
		}
		public String getErro() {
			return erro;
		}
		public void setErro(String erro) {
			this.erro = erro;
		}
}
