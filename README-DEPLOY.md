# Backend - Sanos y Salvos (EKS)

13 servicios Spring Boot (Eureka, API Gateway,BFF-web + 10 microservicios).

## Secrets requeridos en GitHub (Settings → Secrets and variables → Actions)
- `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_SESSION_TOKEN` (si es AWS Academy, estas rotan ~cada 4h: hay que actualizar el secret antes de correr el workflow, o el deploy fallará con error de credenciales expiradas)
- `AWS_REGION` (ej. `us-east-1`)
- `EKS_CLUSTER_NAME` (nombre exacto del clúster)

El job `deploy` usa `aws eks update-kubeconfig`, NO un kubeconfig estático guardado como secreto — así funciona aunque las credenciales de Academy roten.

## Antes de desplegar
Reemplaza `TU_USUARIO_DOCKERHUB` en `k8s/*.yaml`:
```bash
sed -i 's/TU_USUARIO_DOCKERHUB/tu_usuario_real/g' k8s/*.yaml
```

## Orden de despliegue
1. Primero el proyecto **data** (postgres, redis, rabbitmq deben existir).
2. Luego este proyecto.
3. Al final **frontend**.

## api-gateway como LoadBalancer
`03-api-gateway.yaml` usa `type: LoadBalancer` (crea un Classic/NLB de AWS) porque el navegador del usuario llama al gateway directo, no vía DNS interno del clúster. Para obtener la URL pública una vez desplegado:
```bash
kubectl get svc api-gateway -n sanos-salvos -w
```
Espera a que `EXTERNAL-IP` deje de decir `<pending>`. Esa URL (con `http://` y puerto `8080`) es la que necesitas configurar como `VITE_GATEWAY_URL` en el repo de **frontend** antes de compilarlo.

## Si `LoadBalancer` no crea el ELB (IAM restringido por LabRole)
En algunos entornos de AWS Academy el rol del clúster no tiene permisos de `elasticloadbalancing:*`. Si el Service se queda en `<pending>` indefinidamente:
```bash
kubectl describe svc api-gateway -n sanos-salvos   # revisa Events al final
```
Si es un tema de permisos, como fallback usa `kubectl port-forward` para pruebas:
```bash
kubectl port-forward svc/api-gateway -n sanos-salvos 8080:8080
```

## Nodos y recursos
13 pods de Java (más 3 de `data`) piden en total ~2 vCPU / ~5Gi de memoria solo en requests. Si tu node group de Academy es de instancias pequeñas (t3.medium o similar) vas a saturar el clúster igual que te pasó con Notara — si ves pods en `Pending` por falta de recursos, revisa `kubectl describe node` y considera escalar el node group o bajar réplicas/requests.
