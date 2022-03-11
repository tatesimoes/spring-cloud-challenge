package br.com.caelum.eats.pagamento;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;


@Service
public class ClienteRestDoPedido {

    @Autowired
    private PedidoClient pedidoClient;

    void notificaPagamentoDoPedido(Long pedidoId) {

        ResponseEntity<?> response = pedidoClient.atualizaStatusPedido(pedidoId, new PedidoMudancaDeStatusRequest("pago".toUpperCase()));

        if (!HttpStatus.valueOf(response.getStatusCodeValue()).is2xxSuccessful()) {
            throw new RuntimeException("problema ao tentar mudar o status do pedido: " + pedidoId);
        }
    }
}

@FeignClient("monolito")
interface PedidoClient {

    @PutMapping("/pedidos/{numeroDoPedido}/status")
    ResponseEntity<?> atualizaStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoMudancaDeStatusRequest pedidoMudandaStatus);
}

@Getter
@AllArgsConstructor
class PedidoMudancaDeStatusRequest {
    private String status;
}


