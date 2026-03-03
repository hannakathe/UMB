from direct.task import Task
from panda3d.core import Vec3


class Bullet:
    SPEED     = 30    # unidades por segundo
    MAX_RANGE = 50    # distancia máxima antes de destruirse

    def __init__(self, base, start_pos, direction):
        self.base       = base
        self.direction  = Vec3(direction)
        self.direction.normalize()
        self.traveled   = 0.0
        self._task_name = f"bullet_{id(self)}"

        # Esfera pequeña (modelo built-in de Panda3D)
        self.node = base.loader.loadModel("models/misc/sphere")
        self.node.reparentTo(base.render)
        self.node.setScale(0.15)
        self.node.setPos(start_pos)
        self.node.setColorScale(1.0, 0.9, 0.1, 1)   # amarillo neón

        base.taskMgr.add(self.update, self._task_name)

    # ── update ─────────────────────────────────────────────────────────────────
    def update(self, task):
        dt   = globalClock.getDt()
        step = self.SPEED * dt

        pos = self.node.getPos()
        self.node.setPos(pos + self.direction * step)
        self.traveled += step

        if self.traveled >= self.MAX_RANGE:
            self.destroy()
            return Task.done

        return Task.cont

    # ── helpers ────────────────────────────────────────────────────────────────
    def get_pos(self):
        return self.node.getPos()

    def is_alive(self):
        return not self.node.isEmpty()

    def destroy(self):
        self.base.taskMgr.remove(self._task_name)
        if not self.node.isEmpty():
            self.node.removeNode()
