Step to run and deploy:
- go to backend/file-services
	1. command: mvn install 
	to buld the application
	2. command: docker build . -t file-services
	to build the docker image
	3. command: docker tag file-services trial/file-services
	to tag the image <you can use any other names but you need to update the image name in kubernates files/statefulset.yml>
	4. command: docker push trial/file-services
	to push the docker image
- go to kubernates files:
	1. command:  kubectl apply -f ingress.yml
	to add the ingress and will create the pod, please double check that you use the same image name that used in previous steps (3)
	this will create persistent volume which contains the folder which will be shared for both containers
	2. command: kubectl apply -f service.yml
	3. command: kubectl apply -f ingress.yml
- import File Services.postman_collection.json and run the services that you want
Done 