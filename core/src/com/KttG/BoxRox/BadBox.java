package com.KttG.BoxRox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

/*
* Controls all logic for enemies, needs to be remade into multiple classes*/
public class BadBox {
	Image b, j;
	Image shield;
	Image[] orb = new Image[4], laser = new Image[4], explosion = new Image[4];
	float bx, by, speed = 3, gravity = GameScreen.gravity;
	Random r = new Random();
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
	float x = r.nextInt(2);
	float y = r.nextInt(2), big = 0, fade = .1f;
	int[] orbX = new int[4], orbY = new int[4];
	boolean spot, place, alive = true, left = false, right = false, up = false,
			down = false, deadly = true, spinClockwise = false, vulnerable = true, pauseBoo, moving = false, phaseSwap = false, moveBoss = false, hLaser = true, vLaser = false, posL, posR, posT, posB, cornerSwapping = true;
	Rectangle bArea;
	Rectangle[] lArea = new Rectangle[4];
	int frame = 0, spinTimer, laserTimer, bossType, spinDir, minionDir, timer3,
			timer4, minionCount = -1, pauseTimer, moveBossCount = 0, laserSwapTimer, timer0, timer1;
	boolean boss = false, minion = false, pEffect;
	boolean[] phase = new boolean[7];
	int health = 3;
	Group robotGroup = new Group();

	BadBox(boolean boss, boolean minion, int minionDir) {
		this.boss = boss;
		this.minion = minion;
		this.minionDir = minionDir;
		if (boss == false && minion == false) {
			big = r.nextInt(41);
			if (big == 5) {
				b = new Image(Assets.minionSkin.getDrawable("badbox0"));
				b.setSize(60, 60);
				speed = 2;
				GameScreen.bounty += 2;
				health = 10;
			} else {
				b = new Image(Assets.minionSkin.getDrawable("boxPurple0"));
				b.setSize(30, 30);
			}
			spot = r.nextBoolean();
			// System.out.println(spot+" "+x+" "+y);
			if (spot == false) {
				if (x == 0) {
					x = (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()) - 20;
					y = r.nextInt(Gdx.graphics.getHeight() + 1);
					place = false;
				} else {
					x = Gdx.graphics.getWidth();
					y = r.nextInt(Gdx.graphics.getHeight() + 1);
					place = true;
				}
			}
			if (spot == true) {
				if (y == 0) {
					y = (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()) - 20;
					x = r.nextInt(Gdx.graphics.getWidth() + 1);
					place = false;
				} else {
					y = Gdx.graphics.getHeight();
					x = r.nextInt(Gdx.graphics.getWidth() + 1);
					place = true;
				}
			}
			bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(),
					b.getHeight());
		} else if (boss == true && minion == false) {
			bossType = r.nextInt(2);
		//	bossType = 2; // ///////////////////////////////////////////////////////////////////////////////////////////////////////
			if (bossType == 0) {
				b = new Image(Assets.largeSkin.getDrawable("bossBlue0"));
				b.setSize(230, 230);
				x = w / 2 - b.getWidth() / 2;
				y = h + h / 2;
				bArea = new Rectangle(b.getX() + 15, b.getY() + 15,
						b.getWidth() - 60, b.getHeight() - 60);
				health = 100;
				b.setOrigin(b.getWidth() / 2, b.getHeight() / 2);
			}
			if (bossType == 1) {
				deadly = false;
				b = new Image(Assets.largeSkin.getDrawable("bossGreen0"));
				b.setSize(170, 170);
				x = w / 2 - b.getWidth() / 2;
				y = h / 2 - b.getHeight() / 2;
				b.setColor(1, 1, 1, 0f);
				phase[0] = true;
				bArea = new Rectangle(b.getX() + 15, b.getY() + 15,
						b.getWidth() - 30, b.getHeight() - 30);
				health = 50;
				b.setOrigin(b.getWidth() / 2, b.getHeight() / 2);
			}
			if (bossType == 2) {
				b = new Image(Assets.largeSkin.getDrawable("bossRobot0"));
				// b.setSize(200, 200);
				phase[0] = true;
				bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				health = 70; //100 ///////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////////////////////////////////////////////////
				x = w / 2 - b.getWidth() / 2;
				y = h;
				speed = 1.5f;
				orb[3] = new Image(Assets.minionSkin.getDrawable("robotPoint30"));
				orb[2] = new Image(Assets.minionSkin.getDrawable("robotPoint20"));
				orb[1] = new Image(Assets.minionSkin.getDrawable("robotPoint10"));
				orb[0] = new Image(Assets.minionSkin.getDrawable("robotPoint00"));
				shield = new Image(Assets.largeSkin.getDrawable("bossRobotShield0"));
				laser[0] = new Image(Assets.largeSkin.getDrawable("robotLaser00"));
				laser[1] = new Image(Assets.largeSkin.getDrawable("robotLaser10"));
				laser[2] = new Image(Assets.largeSkin.getDrawable("robotLaser20"));
				laser[3] = new Image(Assets.largeSkin.getDrawable("robotLaser30"));
				// GameScreen.bossGroup.addActor(shield);
				robotGroup.addActor(shield);
				vulnerable = false;
				for (int i = 0; i < 4; i++) {
					robotGroup.addActor(orb[i]);
					laser[i].setVisible(false);
					robotGroup.addActor(laser[i]);
					lArea[i] = new Rectangle(laser[i].getX(), laser[i].getY(), laser[i].getWidth(), laser[i].getHeight());
					explosion[i] = new Image(Assets.shipSkin.getDrawable("bossRobotExplode0"));
					robotGroup.addActor(explosion[i]);
					explosion[i].setVisible(false);
					explosion[i].setSize(60, 60);
				}
				GameScreen.playerGroup.addActor(robotGroup);
			}
			GameScreen.bossBattle = true;
			GameScreen.minDifficulty -= 1;
		} else if (boss == false && minion == true && GameScreen.bossBox.bossType == 0) {
			b = new Image(Assets.minionSkin.getDrawable("minionBlue0"));
			b.setSize(30, 30);
			x = (GameScreen.bossBox.getB().getX() + (GameScreen.bossBox.b
					.getWidth() / 2));
			y = (GameScreen.bossBox.getB().getY() + (GameScreen.bossBox.b
					.getHeight() / 2));
			bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(),
					b.getHeight());
			spinTimer = r.nextInt(8);
			health = 8;
			if (GameScreen.bossBox.phase[2] == true)
				speed += (gravity * 5);
		} else if (boss == false && minion == true && GameScreen.bossBox.bossType == 1) {
			b = new Image(Assets.minionSkin.getDrawable("minionGreen0"));
			b.setSize(30, 30);
			x = (GameScreen.bossBox.getB().getX() + (GameScreen.bossBox.b
					.getWidth() / 2));
			y = (GameScreen.bossBox.getB().getY() + (GameScreen.bossBox.b
					.getHeight() / 2));
			speed += (6);
			bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(),
					b.getHeight());
			health = 8;
		} else if (boss == false && minion == true && GameScreen.bossBox.bossType == 2) {
			b = new Image(Assets.minionSkin.getDrawable("robotMinion0"));
			j = new Image(Assets.minionSkin.getDrawable("robotMinionBlast3"));
			j.setSize(186, 186);
			GameScreen.minionGroup.addActor(j);
			j.setVisible(false);
			b.setSize(40, 40);
			x = (GameScreen.bossBox.getB().getX() + (GameScreen.bossBox.b.getWidth() / 2)-b.getWidth()/2);
			y = (GameScreen.bossBox.getB().getY() + (GameScreen.bossBox.b.getHeight() / 2)-b.getHeight()/2);
			speed -= 1;
			bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			health = 12;
			if (GameScreen.bossBox.phase[4] == true) timer4 = -100;
		}
		b.setPosition(x, y);
		if (boss && bossType == 2){
			robotGroup.addActor(b);
		} else GameScreen.minionGroup.addActor(b);
	}

	public void dispose() {
		b.setPosition(-10000, -10000);
		b = null;
		for (int i=0;i<4;i++){
			if (orb[i] != null) orb[i].setPosition(-10000,  -10000);
			if (laser[i] != null) laser[i].setPosition(-10000,  -10000);
			orb[i] = null;
			laser[i] = null;
		}
		if (shield != null) shield.setPosition(-10000, -10000);
		// t = null;
	}

	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************
	// ******************************************************************************************************************************************

	void update() {
		if (boss == false && minion == false) {
			bArea.setPosition(b.getX(), b.getY());
			if (spot == false) {
				if (place == false) {
					x += speed;
					y -= gravity;
					if (x > Gdx.graphics.getWidth() + 50)
						alive = false;
				} else {
					x -= speed;
					y -= gravity;
					if (x < (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()) - 50)
						alive = false;
				}
			} else {
				if (place == false) {
					y += speed;
					y -= gravity;
					if (y > Gdx.graphics.getHeight() + 50)
						alive = false;
				} else {
					y -= speed;
					y -= gravity;
					if (y < (Gdx.graphics.getHeight() - Gdx.graphics
							.getHeight()) - 50)
						alive = false;
				}
			}
		}
		// ******************************************************************************************************************************************
		// **************************************************************MINION MOVEMENT**********************************************************************
		// ******************************************************************************************************************************************
		else if (boss == false && minion == true && GameScreen.bossBox.bossType == 0) {
			bArea.setPosition(b.getX(), b.getY());
			if (GameScreen.bossBox.phase[0] == true || GameScreen.bossBox.phase[1] == true) y -= speed + (gravity * 5);
			if (GameScreen.bossBox.phase[2] == true || GameScreen.bossBox.phase[3] == true) {
				if (y < (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()) - 50)
					alive = false;
				if (y > Gdx.graphics.getHeight() + 50)
					alive = false;
				if (x < (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()) - 50)
					alive = false;
				if (x > Gdx.graphics.getWidth() + 50)
					alive = false;
				if (spinTimer == 0)
					x += speed;
				if (spinTimer == 1)
					x -= speed;
				if (spinTimer == 2)
					y += speed;
				if (spinTimer == 3)
					y -= speed;
				if (spinTimer == 4) {
					x += speed;
					y += speed;
				}
				if (spinTimer == 5) {
					x -= speed;
					y += speed;
				}
				if (spinTimer == 6) {
					x += speed;
					y -= speed;
				}
				if (spinTimer == 7) {
					x -= speed;
					y -= speed;
				}
			}
		} else if (boss == false && minion == true && GameScreen.bossBox.bossType == 1) {
			bArea.setPosition(b.getX(), b.getY());
			if (y < (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()) - 50)
				alive = false;
			if (y > Gdx.graphics.getHeight() + 50)
				alive = false;
			if (x < (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()) - 50)
				alive = false;
			if (x > Gdx.graphics.getWidth() + 50)
				alive = false;
			if (minionDir == 0)
				x += speed;
			if (minionDir == 1)
				x -= speed;
			if (minionDir == 2)
				y += speed;
			if (minionDir == 3)
				y -= speed;
			if (minionDir == 4) {
				x += speed;
				y += speed;
			}
			if (minionDir == 5) {
				x += speed;
				y -= speed;
			}
			if (minionDir == 6) {
				x -= speed;
				y += speed;
			}
			if (minionDir == 7) {
				x -= speed;
				y -= speed;
			}
		} else if (boss == false && minion == true && GameScreen.bossBox.bossType == 2) {
			if (y < (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()) - 50)
				alive = false;
			if (y > Gdx.graphics.getHeight() + 50)
				alive = false;
			if (x < (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()) - 50)
				alive = false;
			if (x > Gdx.graphics.getWidth() + 50)
				alive = false;
			timer3+= 1;
			if (timer3 == 60){
				minionDir = minionDir+4;
				speed+=.4f;
			}
			if (timer3 <= 120) {
				if (minionDir == 0)
					x += speed;
				if (minionDir == 1)
					x -= speed;
				if (minionDir == 2)
					y += speed;
				if (minionDir == 3)
					y -= speed;
				if (minionDir == 4) {
					x += speed;
					y -= speed;
				}
				if (minionDir == 5) {
					x -= speed;
					y += speed;
				}
				if (minionDir == 6) {
					x += speed;
					y += speed;
				}
				if (minionDir == 7) {
					x -= speed;
					y -= speed;
				}
			}
			spinBoss();
			timer4+=1;
			if (timer4 == 150) {
				float tx = b.getX()+b.getWidth()/2, ty = b.getY()+b.getHeight()/2;
				b.setSize(50, 50);
				b.setDrawable(Assets.minionSkin.getDrawable("robotMinionBlast0"));
				bArea.setSize(b.getWidth(), b.getHeight());
				tx = (b.getX()+b.getWidth()/2)-tx;
				ty = (b.getY()+b.getHeight()/2)-ty;
				x-=tx;
				y-=ty;
				//x-=12.5f; y-=12.5f;
			}
			if (timer4 == 190) {
				float tx = b.getX()+b.getWidth()/2, ty = b.getY()+b.getHeight()/2;
				b.setSize(78, 78);
				b.setDrawable(Assets.minionSkin.getDrawable("robotMinionBlast1"));
				bArea.setSize(b.getWidth(), b.getHeight());
				tx = (b.getX()+b.getWidth()/2)-tx;
				ty = (b.getY()+b.getHeight()/2)-ty;
				x-=tx;
				y-=ty;
				vulnerable = false;
				//x-=19.5f; y-=19.5f;
			}
			if (timer4 == 230) {
				float tx = b.getX()+b.getWidth()/2, ty = b.getY()+b.getHeight()/2;
				b.setSize(140, 140);
				b.setDrawable(Assets.minionSkin.getDrawable("robotMinionBlast2"));
				bArea.setSize(b.getWidth(), b.getHeight());
				tx = (b.getX()+b.getWidth()/2)-tx;
				ty = (b.getY()+b.getHeight()/2)-ty;
				x-=tx;
				y-=ty;
				//x-=35; y-=35;
			}
//			if (timer4 == 340) { //IN HERE
//				float tx = b.getX()+b.getWidth()/2, ty = b.getY()+b.getHeight()/2;
////				b.setVisible(false);
////				j.setVisible(true);
//				b.setDrawable(Assets.minionSkin.getDrawable("robotMinionBlast3"));
//				b.setSize(186, 186); //WHAT THE ACTUAL FUCK
//				bArea.setSize(b.getWidth(), b.getHeight());
//				tx = (b.getX()+b.getWidth()/2)-tx;
//				ty = (b.getY()+b.getHeight()/2)-ty;
//				x-=tx;
//				y-=ty;
////				j.setPosition(x, y);
////				//x-=46.5f; y-=46.5f;
//			}
			if (timer4 == 400) { 
				timer4 = 0;
				vulnerable = true;
				float tx = b.getX()+b.getWidth()/2, ty = b.getY()+b.getHeight()/2;
				j.setVisible(false);
				b.setVisible(true);
				b.setSize(40, 40);
				b.setDrawable(Assets.minionSkin.getDrawable("robotMinion0"));
				bArea.setSize(b.getWidth(), b.getHeight());
				tx = tx-(b.getX()+b.getWidth()/2);
				ty = ty-(b.getY()+b.getHeight()/2);
				x+=tx;
				y+=ty;
			}
			bArea.setPosition(x, y);
		}
		// ******************************************************************************************************************************************
		// **************************************************************TYPE0**********************************************************************
		// ******************************************************************************************************************************************
		else if (boss == true && minion == false) {
			if (bossType == 0) {
				bArea.setPosition(b.getX() + 30, b.getY() + 30);
				if (phase[1] == false && phase[2] == false && phase[3] == false) {
					if (y > h - b.getHeight()) {
						y -= 2;
					} else if (y < h - b.getHeight() - 1) {
						y += 1;
					}
				}
				if (y <= h - b.getHeight() && phase[0] == false
						&& phase[1] == false && phase[2] == false
						&& phase[3] == false) {
					spinTimer++;
					if (spinTimer == 200) {
						spinTimer = 0;
						phase[0] = true;
						left = r.nextBoolean();
						if (left == false)
							right = true;
					}
				}
				if (phase[0] == true) {
					spinTimer++;
					laserTimer++;
					if (spinTimer == 30) {
						spinTimer = 0;
						GameScreen.tempBox = GameScreen.createBox(false, true, 0);
						GameScreen.bounty += 1;
						GameScreen.minionGroup.addActor(GameScreen.tempBox.getB());
						GameScreen.enemyList.add(GameScreen.tempBox);
						GameScreen.cleanList();
					}
					if (left == true) {
						x -= 2;
						if (x <= w - w - (b.getWidth() / 2)) {
							left = false;
							right = true;
						}
					}
					if (right == true) {
						x += 2;
						if (x >= w - (b.getWidth() / 2)) {
							right = false;
							left = true;
						}
					}
					if (laserTimer == 1000) {
						laserTimer = 0;
						phase[0] = false;
						phase[1] = true;
						up = r.nextBoolean();
						if (up == false)
							down = true;
					}
				}
				if (phase[1] == true) {
					laserTimer++;
					if (left == true) {
						x -= 5;
						if (x <= w - w - (b.getWidth() / 2)) {
							left = false;
							right = true;
						}
					}
					if (right == true) {
						x += 5;
						if (x >= w - (b.getWidth() / 2)) {
							right = false;
							left = true;
						}
					}
					if (up == true) {
						y += 2;
						if (y >= h - b.getHeight() / 2) {
							up = false;
							down = true;
						}
					}
					if (down == true) {
						y -= 2;
					}
					if (y <= h - h - b.getHeight() / 2) {
						down = false;
						up = true;
					}
					if (laserTimer == 500) {
						laserTimer = 0;
						phase[1] = false;
						phase[2] = true;
					}
				}
				if (phase[2] == true) {
					laserTimer++;
					spinTimer++;
					if (spinTimer == 20) {
						spinTimer = 0;
						makeMinion(0);
					}
					if (left == true) {
						x -= 4;
						if (x <= w - w - (b.getWidth() / 2)) {
							left = false;
							right = true;
						}
					}
					if (right == true) {
						x += 4;
						if (x >= w - (b.getWidth() / 2)) {
							right = false;
							left = true;
						}
					}
					if (up == true) {
						y += 3;
						if (y >= h - b.getHeight() / 2) {
							up = false;
							down = true;
						}
					}
					if (down == true) {
						y -= 3;
					}
					if (y <= h - h - b.getHeight() / 2) {
						down = false;
						up = true;
					}
					if (laserTimer == 1500) {
						laserTimer = 0;
						phase[1] = false;
						phase[2] = false;
						phase[3] = true;
						left = false;
						right = false;
						up = false;
						down = false;
					}
				}
				if (phase[3] == true) {
					spinTimer++;
					if (spinTimer == 30) {
						spinTimer = 0;
						makeMinion(0);
					}
					if (laserTimer == 0) { // restart fight
						for (int i = 0; i < GameScreen.enemyList.size(); i++) {
							if (GameScreen.enemyList.get(i).minion == true) {
								GameScreen.enemyList.get(i).alive = false;
							}
						}
						phase[3] = false;
						phase[2] = false;
						phase[1] = false;
						phase[0] = false;
						spinTimer = 199;
						laserTimer = 0;
						spinDir = 0;
						timer3 = 0;
						timer4 = 0;
						if (x > w / 2 - b.getWidth() / 2)
							x -= 1;
						if (x < w / 2 - b.getWidth() / 2)
							x += 1;
						if (y > h / 2 - b.getHeight() / 2)
							y -= 1;
						if (y < h / 2 - b.getHeight() / 2)
							y += 1;

					}
					if (laserTimer == 1) {
						y -= 2;
						if (y < h - h - b.getHeight()) {
							alive = false;
							laserTimer = 0;
						}
					}
				}// ******************************************************************************************************************************************
			} // *************************************************************TYPE 1**********************************************************************
			if (bossType == 1) { // ***********************************************************************************************************************
				bArea.setPosition(b.getX() + 15, b.getY() + 15);
				if (phase[0] == true) {
					if (timer3 < 150)
						timer3++;
					if (timer3 >= 150) {
						if (spinTimer == 0) {
							laserTimer++;
							if (laserTimer == 12) {
								laserTimer = 0;
								fade += .1f;
								b.setColor(1, 1, 1, fade);
							}
							if (fade >= 1) {
								fade = 1;
								deadly = true;
								if (GameScreen.b.getX() >= x
										+ (b.getWidth() / 2))
									right = true;
								if (GameScreen.b.getX() <= x
										- (b.getWidth() / 2))
									left = true;
								if (GameScreen.b.getY() >= y
										+ (b.getHeight() / 2))
									up = true;
								if (GameScreen.b.getY() <= y
										- (b.getHeight() / 2))
									down = true;
								if (left == false && right == false
										&& up == false && down == false
										|| left == true && right == true
										&& up == true && down == true) {
									right = false;
									left = false;
									up = false;
									down = false;
									if (GameScreen.b.getX() >= x)
										right = true;
									if (GameScreen.b.getX() <= x)
										left = true;
									if (GameScreen.b.getY() >= y)
										up = true;
									if (GameScreen.b.getY() <= y)
										down = true;
								}
								spinTimer = 1;
							}
						}
						if (spinTimer == 1) {
							if (right)
								x += 7;
							if (left)
								x -= 7;
							if (up)
								y += 7;
							if (down)
								y -= 7;
							if (x > w - (b.getWidth() / 2)
									|| x < w - w - (b.getWidth() / 2)
									|| y > h - (b.getHeight() / 2)
									|| y < h - h - (b.getHeight() / 2)) {
								spinTimer = 2;
								deadly = false;
							}
						}
						if (spinTimer == 2) {
							laserTimer++;
							if (laserTimer == 6) {
								laserTimer = 0;
								fade -= .1f;
								b.setColor(1, 1, 1, fade);
							}
							if (fade <= 0) {
								fade = 0;
								x = GameScreen.b.getX() - b.getWidth() / 2;
								y = GameScreen.b.getY() - b.getHeight() / 2;
								spinTimer = 0;
								left = false;
								right = false;
								up = false;
								down = false;
								spinDir++;
								if (spinDir == 6) {
									phase[0] = false;
									phase[1] = true;
									spinDir = 0;
									spinTimer = 0;
									laserTimer = 0;
									timer3 = 1;
									x = w / 2 - b.getWidth() / 2;
									y = h / 2 - b.getHeight() / 2;
								}
							}
						}
					}
				}
				if (phase[1] == true) {
					laserTimer++;
					if (laserTimer % 50 == 0) { // Randomize Movement
						left = false;
						right = false;
						up = false;
						down = false;
						left = r.nextBoolean();
						up = r.nextBoolean();
						if (left == false)
							right = true;
						if (up == false)
							down = true;
					}
					if (laserTimer == 200) { // Cycle shooting angle
						laserTimer = 0;
						if (spinDir == 0)
							spinDir = 4;
						else
							spinDir = 0;
					}
					if (timer3 == 0) { // Move
						spinTimer++;
						if (right)
							x += 1;
						if (left)
							x -= 1;
						if (up)
							y += 1;
						if (down)
							y -= 1;
						if (x > w - (b.getWidth() / 2)
								|| x < w - w - (b.getWidth() / 2)
								|| y > h - (b.getHeight() / 2)
								|| y < h - h - (b.getHeight() / 2)) { // Offscreen
							timer3 = 1;
							deadly = false;
						}
					}
					if (timer3 != 0) {
						timer3++;
					}
					if (timer3 == 4) { // Fade Out
						timer3 = 1;
						fade -= .1f;
						b.setColor(1, 1, 1, fade);
						if (fade <= 0) {
							fade = 0;
							x = GameScreen.b.getX() - b.getWidth() / 2;
							y = GameScreen.b.getY() - b.getHeight() / 2;
							timer3 = 10;
						}
					}
					if (timer3 == 15) { // Fade In
						timer3 = 10;
						fade += .1f;
						b.setColor(1, 1, 1, fade);
						if (fade >= 1) {
							fade = 1;
							deadly = true;
							timer3 = 0;
						}
					}
					if (spinTimer == 35) { // Spawn Minions
						spinTimer = 0;
						makeMinion(0+spinDir);
						makeMinion(1+spinDir);
						makeMinion(2+spinDir);
						makeMinion(3+spinDir);
					}
					timer4++;
					if (timer4 >= 1000 && fade == 0) {
						phase[1] = false;
						phase[2] = true;
						spinTimer = 0;
						laserTimer = 0;
						spinDir = 0;
						timer3 = 0;
						timer4 = 0;
						x = w / 2 - (b.getWidth() / 2);
						y = h / 2 - (b.getHeight() / 2);
					}
				}
				if (phase[2] == true) {
					if (laserTimer == 0)
						timer3++;
					if (timer3 == 15) { // Fade In
						timer3 = 0;
						fade += .1f;
						b.setColor(1, 1, 1, fade);
						if (fade >= 1) {
							fade = 1;
							deadly = true;
							timer3 = 0;
							laserTimer = 1;
							phase[2] = false;
							phase[0] = true;
						}
					}
					if (laserTimer == 1)
						spinTimer++;
					if (spinTimer == 1) {
						spinTimer = 0;
						y -= 2;
						if (y < h - h - b.getHeight()) {
							alive = false;
						}
					}
				}
			}// ******************************************************************************************************************************************
		} // *************************************************************TYPE2**********************************************************************
		if (bossType == 2) { // ***********************************************************************************************************************
			bArea.setPosition(b.getX(), b.getY());
			if (phase[0]){
//				spinTimer++;
//				if (spinTimer == 200) {
//					spinTimer = 0;
//					spinClockwise = r.nextBoolean();
//				}
//				if (spinClockwise) {
//					spinDir -= 1;
//					if (spinDir <= 0)
//						spinDir = 360;
//				} else {
//					spinDir += 1;
//					if (spinDir >= 360)
//						spinDir = 0;
//				}
				y -= speed;
			if (y <= h / 2 - b.getHeight() / 2) {
				phase[1] = true; //////////////////////////////////////////////////////////////////////////////////
				phase[0] = false;
				System.out.println("PHASE 1");
			}
			}
			if (phase[1]) {
//				spinTimer++;
				pauseTimer++;
//				if (spinTimer == 120) {
//					spinTimer = 0;
//					spinClockwise = r.nextBoolean();
//				}
				if (!phaseSwap){
					spinBoss();
//				if (spinClockwise) {
//					spinDir -= 1;
//					if (spinDir <= 0)
//						spinDir = 360;
//				} else {
//					spinDir += 1;
//					if (spinDir >= 360)
//						spinDir = 0;
//				}
				} else {
					if (spinDir >= 180 && spinDir != 0) spinDir+=1;
					else if (spinDir < 180 && spinDir != 0) spinDir-=1;
					if (spinDir == 360 || spinDir == 0){
						spinDir = 0;
					}
				}
					//Spawn Shield bros
				if (r.nextInt(20) == 0 && minionCount < 7 && pauseBoo == false){
					makeMinion(minionCount);
					if (minionCount >= r.nextInt(4)+4){
						pauseBoo = true;
						phaseSwap = true;
					}
				}
				else if (pauseTimer >= 250 && phaseSwap && spinDir == 0){
					phase[1] = false;
					phase[2] = true;
					timer3 = 0;
					timer4 = 0;
					pauseTimer = 0;
					pauseBoo = false;
					minionCount = 0;
					phaseSwap = false;
					System.out.println("PHASE 2");
				}	
			}
			if (phase[2]){
//				spinTimer++;
//				if (spinTimer == 200) {
//					spinTimer = 0;
//					spinClockwise = r.nextBoolean();
//				}
//				if (spinClockwise) {
//					spinDir -= 1f;
//					if (spinDir <= 0)
//						spinDir = 360;
//				} else {
//					spinDir += 1f;
//					if (spinDir >= 360)
//						spinDir = 0;
//				}
				if (x > w-w && timer3 < 1) x-=speed; 
				if (x <= w-w && timer3 < 1){ timer3 = 1; System.out.println(timer3);}
				if (y < h-b.getHeight() && timer4 < 1) y+=speed; 
				if (y >= h-b.getHeight() && timer4 < 1){ timer4 = 1; System.out.println(timer4);}
				if (timer3 == 1 && timer4 == 1){
					speed = 5;
					pauseBoo = true;
				}
				if (pauseBoo) pauseTimer++;
					if (pauseTimer >= 60 && timer3 == 1){ right = true; timer3 = 2;}
					if (x < w-b.getWidth()-orb[0].getWidth() && right) x+=speed;
					if (x > w-b.getWidth()-orb[0].getWidth() && right){ timer3 = 3; pauseTimer = 0; right = false;}
					if (pauseTimer == 10 && timer3 == 3) down = true;
					if (y > h-h+orb[0].getHeight() && down) y-=speed; 
					if (y < h-h+orb[0].getHeight() && down){ down = false; pauseTimer = 0; timer3 = 4;}
					if (pauseTimer == 10 && timer3 == 4) left = true;
					if (x > w-w+orb[0].getWidth() && left) x-=speed;
					if (x <= w-w+orb[0].getWidth() && left){ timer3 = 5; pauseTimer = 0; left = false;}
					if (pauseTimer == 10 && timer3 == 5) up = true;
					if (y < h-b.getHeight()-orb[0].getHeight() && up) y+=speed;
					if (y >= h-b.getHeight()-orb[0].getHeight() && up){ up = false; pauseTimer = 0; timer3 = 6;}
					if (pauseTimer == 10 && timer3 == 6){
						phase[2] = false;
						phase[3] = true;
						pauseBoo = false;
						timer3 = 0;
						timer4 = 0;
						speed = 2.5f;
					System.out.println("PHASE 3");
				}
			}
//		}
			if (phase[3]){
				if (spinDir != 0){
					spinDir-=1;
					if (spinDir < 0) spinDir = 0;
				}
				if (spinDir == 0){
					pauseBoo = true;
				}
				if (pauseBoo) pauseTimer++;
				if (pauseTimer <= 10){
					for (int i=0;i<4;i++){
						orb[i].setDrawable(Assets.minionSkin.getDrawable("robotPointEffect"+i+""+r.nextInt(2)));
					}
				}
				else if (pauseTimer <= 80){
					for (int i=0;i<4;i++){
						int l = r.nextInt(2)+2;
						orb[i].setDrawable(Assets.minionSkin.getDrawable("robotPointEffect"+i+""+l));
					}
				}
				else if (pauseTimer <= 120){
						for (int i=0;i<4;i++){
							int k = r.nextInt(2)+4;
							orb[i].setDrawable(Assets.minionSkin.getDrawable("robotPointEffect"+i+""+k));
						}
						pEffect = true;
						moveBoss = true;
						for (int i=0;i<4;i++){
							laser[i].setVisible(true);
						}
				}
			if (moveBoss){
				if (!moving){
					moving = true;
				int dir = r.nextInt(2);
				switch (dir){
				case 0: 
					if (x <= w-w+orb[0].getWidth()){ right = true;
					} else left = true;
					break;
				case 1: 
					if (y <= h-h+b.getHeight()+orb[0].getHeight()){ up = true;
					} else down = true;
					break;
//				case 2: 
//					if (y <= h){ up = true;
//					} else down = true;
//					if (x <= w){ right = true;
//					} else left = true;
//					break;
				}
				} else if (moving){
			moving = false;
			fireLaser();
			swapWalls();
			if (!moving && pEffect == true){
				left = false; right = false; up = false; down = false;
				moveBossCount+=1;
				System.out.println("moveBossCount: "+moveBossCount);
				if (moveBossCount >= 4){
					System.out.println("Ending phase[3]");
					phase[2] = false;
					phase[3] = false;
					phase[4] = true;
					moving = false;
					moveBoss = false;
					timer3 = 0;
					moveBossCount = 0;
					pauseTimer = r.nextInt(150)*-1;
					cornerSwapping = true;
					}
				}
				}
			}
		}
			if (phase[4]){
				if (cornerSwapping == true){
					cornerSwap();
					fireLaser();
					timer3++;
					if (timer3 == 50 && minionCount < 7){
						timer3 = 0;
						makeMinion(r.nextInt(4));
					}
				} else {
					phase[4] = false;
					phase[3] = true;
					moveBossCount = r.nextInt(3);
					timer3 = 0;
					endLaser();
				}
			}
			if (health <= 50 && phase[5] == false){
				phase[0] = false;
				phase[1] = false;
				phase[2] = false;
				phase[3] = false;
				phase[4] = false;
				endLaser();
				timer3 = 0;
				moveBoss = false;
				moveBossCount = 0;
				timer4 = 0;
				destroyEnds();
				
				
			}
			if (phase[5]){
				centerBoss(speed/2);
				timer1++;
				if (timer1 >= 300) spinBoss();
				if (timer1 == 350){
					makeMinion(10);
					makeMinion(11);
					makeMinion(12);
					makeMinion(13);
					makeMinion(14);
					makeMinion(15);
				}
			}
		}
		// **********************************************************************************************************************************
		// **********************************************************************************************************************************
		// **********************************************************FRAME UPDATE************************************************************
		// **********************************************************FRAME UPDATE************************************************************
		// **********************************************************************************************************************************
		// **********************************************************************************************************************************
		b.setPosition(x, y);
		frame++;
		if (boss == false && minion == true && GameScreen.bossBox.bossType == 2) {
			b.setOrigin(b.getWidth()/2, b.getHeight()/2);
			b.setRotation(spinDir);
			j.setPosition(x, y);
			j.setOrigin(j.getWidth()/2, j.getHeight()/2);
			j.setRotation(spinDir);
		}
		if (boss == true && bossType == 2) {
			orb[0].setPosition(b.getX() + b.getWidth(), b.getY() + b.getHeight() / 2 - orb[0].getHeight() / 2);
			orb[1].setPosition(b.getX() + b.getWidth() / 2 - orb[1].getWidth()/ 2, b.getY() + b.getHeight());
			orb[2].setPosition(b.getX() - orb[2].getWidth(),b.getY() + (b.getHeight() / 2) - orb[0].getHeight() / 2);
			orb[3].setPosition(b.getX() + b.getWidth() / 2 - orb[3].getWidth()/ 2, b.getY() - orb[3].getHeight());
			for (int i=0; i<4;i++){
				if (explosion[i].isVisible()){
					explosion[i].setPosition(orb[i].getX(), orb[i].getY());
			}
			}
			if (vulnerable == false && health > 20) {
				shield.setPosition(x, y);
			} else {
				shield.setPosition(-10000, -10000);
			}
			if (pEffect == true){
				if (vLaser){
				laser[0].setPosition(b.getX()+b.getWidth()+(orb[0].getWidth()/1.7f), b.getY()+b.getWidth()/2-laser[0].getHeight());
				laser[2].setPosition(b.getX()-(orb[0].getWidth()/1.7f)-laser[2].getWidth(), b.getY()+b.getHeight()/2-laser[2].getHeight()/2);
				laser[1].setPosition(-10000, -10000);
				laser[3].setPosition(-10000, -10000);
				} else if (hLaser){
					laser[0].setPosition(-10000, -10000);
					laser[2].setPosition(-10000, -10000);
					laser[1].setPosition(b.getX()+b.getWidth()/2-laser[1].getWidth()/2, b.getY()-laser[1].getHeight()-(orb[3].getHeight()/1.7f));
					laser[3].setPosition(b.getX()+b.getWidth()/2-laser[3].getWidth()/2, b.getY()+b.getHeight()+(orb[1].getHeight()/1.7f));
				}
				if (!hLaser && !vLaser){
					laser[0].setPosition(-10000, -10000);
					laser[1].setPosition(-10000, -10000);
					laser[2].setPosition(-10000, -10000);
					laser[3].setPosition(-10000, -10000);
				}
			}
			robotGroup.setOrigin(b.getX() + b.getWidth() / 2,
					b.getY() + b.getHeight() / 2);
			// robotGroup.setOrigin(0);
			robotGroup.setRotation(spinDir);
			if (pEffect){
				for (int i=0;i<4;i++){
					lArea[i].setPosition(laser[i].getX(), laser[i].getY());
				}} else {
					for (int i=0;i<4;i++){
						lArea[i].setPosition(-10000, -10000);
					}
				}
			}
		if (frame == 10) {
			frame = 0;
			if (boss == true) {
				if (bossType == 0) {
					b.setDrawable(Assets.largeSkin.getDrawable("bossBlue"
							+ r.nextInt(2)));
					b.setOrigin(b.getWidth() / 2, b.getHeight() / 2);
					if (r.nextBoolean())
						b.setRotation(b.getRotation() + r.nextInt(35) + 10);
					else
						b.setRotation(b.getRotation() - r.nextInt(35) - 10);
				}
				if (bossType == 1) {
					b.setDrawable(Assets.largeSkin.getDrawable("bossGreen"
							+ r.nextInt(2)));
					if (r.nextBoolean())
						b.setRotation(b.getRotation() + r.nextInt(35) + 10);
					else
						b.setRotation(b.getRotation() - r.nextInt(35) - 10);
				}
				if (bossType == 2) {
					shield.setDrawable(Assets.largeSkin.getDrawable("bossRobotShield" + r.nextInt(2)));
					for (int i=0;i<4;i++){
						if (explosion[i].isVisible()){
							System.out.println("explosion"+i+" visible: "+explosion[i].isVisible());
							System.out.println("explosion"+i+" XY: "+explosion[i].getX()+" "+explosion[i].getY());
							if (explosion[i].getDrawable().toString() == "bossRobotExplode2"){
								explosion[i].setVisible(false);
								timer1++;
								orb[i].setDrawable(Assets.minionSkin.getDrawable("robotPoint"+i+"1"));
							}
							if (explosion[i].getDrawable().toString() == "bossRobotExplode1") explosion[i].setDrawable(Assets.shipSkin.getDrawable("bossRobotExplode2"));
							if (explosion[i].getDrawable().toString() == "bossRobotExplode0") explosion[i].setDrawable(Assets.shipSkin.getDrawable("bossRobotExplode1"));
							if (timer1 == 4) phase[5] = true;
						}
					}
					if (pEffect){
						for (int i=0;i<4;i++){
							int k = r.nextInt(2)+4;
							orb[i].setDrawable(Assets.minionSkin.getDrawable("robotPointEffect"+i+""+k));
						}
					}
				}
			} else if (minion == true) {
				if (GameScreen.bossBox.bossType == 0) {
					b.setDrawable(Assets.minionSkin.getDrawable("minionBlue"
							+ r.nextInt(8)));
				}
				if (GameScreen.bossBox.bossType == 1) {
					b.setDrawable(Assets.minionSkin.getDrawable("minionGreen"
							+ r.nextInt(8)));
				}
			} else if (big == 5)
				b.setDrawable(Assets.minionSkin.getDrawable("badbox"
						+ r.nextInt(8)));
			else
				b.setDrawable(Assets.minionSkin.getDrawable("boxPurple"
						+ r.nextInt(8)));

		}
	}
	public void fireLaser(){
		vulnerable = true;
		laserSwapTimer+= r.nextInt(5);
		if (laserSwapTimer >= 75){
			laserSwapTimer = 0;
			if (hLaser){
				hLaser = false;
				vLaser = true;
			} else {
				hLaser = true;
				vLaser = false;
			}
		}
	}
	public void endLaser(){
		vulnerable = false;
		pEffect = false;
		hLaser = false;
		vLaser = false;
		laser[0].setPosition(-10000, -10000);
		laser[1].setPosition(-10000, -10000);
		laser[2].setPosition(-10000, -10000);
		laser[3].setPosition(-10000, -10000);
	}
	public void destroyEnds(){
		if (r.nextInt(20) == 0 && timer0 < 4){
			explosion[timer0].setVisible(true);
			explosion[timer0].setPosition(orb[timer0].getX(), orb[timer0].getY());
			timer0++;
		}
	}
	public void cornerSwap(){
		if (!posL && !posR && !posB && !posT){
			if (x < w/2) posR = true; else posL = true;
			if (y < h/2) posT = true; else posB = true;
		}
		if (posL == true){
			System.out.println("CornerSwap PosL");
			x-= w/600;
			if (x < w-w+orb[0].getWidth()) posL = false;
		}
		if (posR == true){
			System.out.println("CornerSwap PosR");
			x+= w/600;
			if (x > w-b.getWidth()-orb[0].getWidth()) posR = false;
		}
		if (posT == true){
			System.out.println("CornerSwap PosT");
			y+= h/600;
			if (y > h-b.getHeight()-orb[0].getHeight()) posT = false;
		}
		if (posB == true){
			System.out.println("CornerSwap PosB");
			y-= h/600;
			if (y < h-h+orb[0].getHeight()) posB = false;
		}
		if (!posL && !posR && !posB && !posT){
			cornerSwapping = false;
		}
	}
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Image getB() {
		return b;
	}
	public void makeMinion(int i){
		minionCount+=1;
		GameScreen.tempBox = GameScreen.createBox(false, true, i);
		GameScreen.enemyList.add(GameScreen.tempBox);
		GameScreen.minionGroup.addActor(GameScreen.tempBox.getB());
		GameScreen.cleanList();
	}
	public void spinBoss(){
		spinTimer++;
		if (spinTimer == 120) {
			spinTimer = 0;
			spinClockwise = r.nextBoolean();
		}
		if (spinClockwise) {
			spinDir -= 1;
			if (spinDir <= 0)
				spinDir = 360;
		} else {
			spinDir += 1;
			if (spinDir >= 360)
				spinDir = 0;
		}
	}
	public void centerBoss(float s){
		if (x>(w/2)-(b.getWidth()/2)) x-=s;
		if (x<(w/2)-(b.getWidth()/2)) x+=s;
		if (y>(h/2)-(b.getHeight()/2)) y-=s;
		if (y<(h/2)-(b.getHeight()/2)) y+=s;
	}
	public void swapWalls() {
		if (left && x >= w-w+orb[0].getWidth()){ x-=speed; moving = true;}
		if (right && x <= w-b.getWidth()-orb[0].getWidth()){ x+=speed; moving = true;}
		if (up && y <= h-b.getHeight()-orb[0].getHeight()){ y+=speed; moving = true;}
		if (down && y >= h-h+orb[0].getHeight()){ y-=speed; moving = true;}
	}
}
