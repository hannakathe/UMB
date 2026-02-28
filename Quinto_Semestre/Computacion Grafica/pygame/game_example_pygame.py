import pygame, sys
pygame.init()

size = (800, 600)
screen = pygame.display.set_mode(size)
pygame.display.set_caption("My Game")

while True:
    for event in pygame.event.get():
        print(event)
        if event.type == pygame.QUIT:
            sys.exit()

    