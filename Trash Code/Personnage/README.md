Importation dans Eclipse :
	File > Import... > Gradle > Existing Gradle Project


	Bon, alors pour faire simple le principe de LibGDX c'est d'avoir un unique code qui marche sur toutes les plateformes (m�me android btw). Ce code l� est dans le dossier 'core'.

	Les autres dossiers ('desktop', 'android', 'html', 'ios') contiennent le code qui est sp�cifique � la plateforme, basiquement c'est juste le main qui va cr�er l'objet qui va contenir l'application, faire la config etc. DU COUP c'est ces projets qui doivent �tre run pour pouvoir tester (dans notre cas on garde que desktop mais c'est bon � savoir, si on veut en faire une app ce sera d�j� fait).

	Pour la map j'ai utilis� un logiciel qui fait des maps en .tmx qui sont assez bien g�r�es par libGDX (https://www.mapeditor.org/)

Pour le code je conseille de lire dans cet ordre :
- Constants.java
- DesktopLauncher.java
- PiRogue.java
- GameScreen.java
- map.tmx (en bonus)

______________
Pour le lancer

* Run > Run Configurations
* Clic droit sur Java Application, New Configuration
	Name : DesktopLauncher
	Project : pirogue-desktop
	Main class : com.pirogue.game.desktop.DesktopLauncher
* RUN
___________________________________________________
Je vous conseille de changer les r�glages d'eclipse

* Window > Preferences > Run/Debug > Launching
* Tout en bas dans Launch Operations, cocher "Always launch the previously launched application".
Comme �a pour d�marrer le projet vous aurez plus qu'� faire : CTRL + F11
_____________________________________________
Si vous avez un probl�me pour load les images

* Dans pirogue-core, clic droit sur le dossier 'assets', Build Path, Use as source folder.
* A chaque fois que vous ajoutez ou enlevez une image dans le dossier assets, il faut faire clic
  droit n'importe o� dans le panneau de gauche et Refresh.